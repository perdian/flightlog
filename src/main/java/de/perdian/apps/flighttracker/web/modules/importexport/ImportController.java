package de.perdian.apps.flighttracker.web.modules.importexport;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.perdian.apps.flighttracker.business.modules.importexport.ImportExportService;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataLoader;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.FlugstatistikdeCredentials;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.FlugstatistikdeDataLoader;
import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;
import de.perdian.apps.flighttracker.web.support.messages.MessageSeverity;
import de.perdian.apps.flighttracker.web.support.messages.Messages;

@Controller
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    private MessageSource messageSource = null;
    private ImportExportService importExportService = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;

    @ModelAttribute
    public ImportEditor importEditor() {
        return new ImportEditor();
    }

    @RequestMapping(value = "/import/file", method = RequestMethod.GET)
    public String doImportFileGet(Model model) {
        model.addAttribute("fileTypes", Arrays.asList(ImportFileType.values()));
        return "/import/impl/file";
    }

    @RequestMapping(value = "/import/file", method = RequestMethod.POST)
    public String doImportFilePost(@RequestParam("file") MultipartFile file, @RequestParam("fileType") ImportFileType fileType, @ModelAttribute Messages messages, @ModelAttribute ImportEditor importEditor, Locale locale, Model model) {
        if (file == null || file.isEmpty()) {
            return this.doImportFileGet(model);
        } else {
            try {

                log.info("Importing entries from uploaded file using file type: {}", fileType);
                DataLoader<Reader> dataLoader = fileType.getDataLoaderSupplier().get();
                List<DataItem> dataItems = dataLoader.loadDataItems(new InputStreamReader(file.getInputStream(), "UTF-8"));

                log.info("Loaded {} entries from uploaded file using file type: {}", dataItems.size(), fileType);
                return this.doVerify(dataItems, importEditor);

            } catch (Exception e) {

                log.debug("Importing entries from uploaded file failed using file type: {}", fileType, e);
                messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("fileImportFailed", null, locale), e.toString());
                return this.doImportFileGet(model);

            }
        }
    }

    @RequestMapping(value = "/import/flugstatistikde", method = RequestMethod.GET)
    public String doImportFlugstatistikdeGet() {
        return "/import/impl/flugstatistikde";
    }

    @RequestMapping(value = "/import/flugstatistikde", method = RequestMethod.POST)
    public String doImportFlugstatistikdePost(@RequestParam("username") String username, @RequestParam("password") String password, @ModelAttribute Messages messages, @ModelAttribute ImportEditor importEditor, Locale locale) {
        if (StringUtils.isEmpty(username)) {
            messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("usernameMustNotBeEmpty", null, locale), null);
            return this.doImportFlugstatistikdeGet();
        } else if (StringUtils.isEmpty(password)) {
            messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("passwordMustNotBeEmpty", null, locale), null);
            return this.doImportFlugstatistikdeGet();
        } else {
            try {

                log.info("Importing entries from flugstatistik.de");
                FlugstatistikdeCredentials flugstatistikcreCredentials = new FlugstatistikdeCredentials(username, password);
                DataLoader<FlugstatistikdeCredentials> flugstatistikDataLoder = new FlugstatistikdeDataLoader();
                List<DataItem> flugstatistikDataItems = flugstatistikDataLoder.loadDataItems(flugstatistikcreCredentials);

                log.info("Loaded {} entries from flugstatistik.de", flugstatistikDataItems.size());
                return this.doVerify(flugstatistikDataItems, importEditor);

            } catch (Exception e) {

                log.debug("Importing entries from flugstatistik.de failed", e);
                messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("dataImportFailed", null, locale), this.getMessageSource().getMessage(e.toString(), null, locale));
                return this.doImportFlugstatistikdeGet();

            }
        }
    }

    private String doVerify(List<DataItem> dataItems, ImportEditor importEditor) throws Exception {

        for (DataItem dataItem : dataItems) {

            AirlineEntity airlineEntity = StringUtils.isEmpty(dataItem.getAirlineCode()) ? null : this.getAirlinesRepository().loadAirlineByIataCode(dataItem.getAirlineCode());
            if (airlineEntity == null && dataItem.getAirlineName() != null) {
                airlineEntity = this.getAirlinesRepository().loadAirlineByName(dataItem.getAirlineName());
            }
            if (airlineEntity != null) {
                if (StringUtils.isEmpty(dataItem.getAirlineCode())) {
                    dataItem.setAirlineCode(airlineEntity.getIataCode());
                }
                if (StringUtils.isEmpty(dataItem.getAirlineName())) {
                    dataItem.setAirlineName(airlineEntity.getName());
                }
            }

            AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(dataItem.getArrivalAirportCode());
            AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(dataItem.getDepartureAirportCode());
            if (departureAirportEntity != null && arrivalAirportEntity != null) {

                // If we do have the airport information we force the recomputation of the distance to make
                // sure the logics used later on when adding new flights are consistent with the logics that
                // were used during the initial import
                Integer recomputedFlightDistance = FlighttrackerHelper.computeDistanceInKilometers(departureAirportEntity.getLongitude(), departureAirportEntity.getLatitude(), arrivalAirportEntity.getLongitude(), arrivalAirportEntity.getLatitude());
                if (recomputedFlightDistance != null) {
                    dataItem.setFlightDistance(recomputedFlightDistance);
                }

                // Check if we can compute the duration from start and end times, or vice versa create the
                // arrival time from the departure time plus the duration
                boolean departureTimeAvailable = dataItem.getDepartureDateLocal() != null && dataItem.getDepartureTimeLocal() != null;
                boolean arrivalTimeAvailable = dataItem.getArrivalDateLocal() != null && dataItem.getArrivalTimeLocal() != null;
                if (departureTimeAvailable && arrivalTimeAvailable && dataItem.getFlightDuration() == null) {
                    dataItem.setFlightDuration(FlighttrackerHelper.computeDuration(departureAirportEntity.getTimezoneId(), dataItem.getDepartureDateLocal(), dataItem.getDepartureTimeLocal(), arrivalAirportEntity.getTimezoneId(), dataItem.getArrivalDateLocal(), dataItem.getArrivalTimeLocal()));
                } else if (departureTimeAvailable && dataItem.getFlightDuration() != null && departureAirportEntity.getTimezoneId() != null && arrivalAirportEntity.getTimezoneId() != null) {
                    ZonedDateTime departureDateTimeInDepartureZone = dataItem.getDepartureTimeLocal().atDate(dataItem.getDepartureDateLocal()).atZone(departureAirportEntity.getTimezoneId());
                    ZonedDateTime arrivalDateTimeInDepartureZone = departureDateTimeInDepartureZone.plus(dataItem.getFlightDuration());
                    ZonedDateTime arrivalDateTimeInArrivalZone = arrivalDateTimeInDepartureZone.withZoneSameInstant(arrivalAirportEntity.getTimezoneId());
                    dataItem.setArrivalDateLocal(arrivalDateTimeInArrivalZone.toLocalDate());
                    dataItem.setArrivalTimeLocal(arrivalDateTimeInArrivalZone.toLocalTime());
                }

            }

        }

        importEditor.setItems(dataItems.stream().map(ImportEditorItem::new).collect(Collectors.toList()));

        return "/import/verify";

    }

    @RequestMapping(value = "/import/execute", method = RequestMethod.POST)
    public String doExecute(@AuthenticationPrincipal FlighttrackerUser user, @ModelAttribute Messages messages, @ModelAttribute ImportEditor importEditor, Locale locale) {

        List<DataItem> activeDataItems = importEditor.getItems().stream()
            .filter(item -> item.isActive())
            .map(item -> item.getFlight())
            .collect(Collectors.toList());

        log.info("Importing {} flights", activeDataItems.size());
        this.getImportExportService().importDataItems(activeDataItems, user == null ? null : user.getUserEntitiy());

        return "/import/executed";

    }

    MessageSource getMessageSource() {
        return this.messageSource;
    }
    @Autowired
    void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    ImportExportService getImportExportService() {
        return this.importExportService;
    }
    @Autowired
    void setImportExportService(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
