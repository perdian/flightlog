package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.authentication.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/flights/")
class FlightsExchangeController {

    private static final Logger log = LoggerFactory.getLogger(FlightsExchangeController.class);

    private FlightsExchangeService exchangeService = null;
    private UserHolder userHolder = null;

    @RequestMapping("/import/file")
    String doImportFileGet() {
        return "flights/import";
    }

    @PostMapping(value = "/import/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String doImportFilePost(@ModelAttribute("exchangeEditor") FlightsExchangeEditor exchangeEditor, Model model) {
        try {
            if (exchangeEditor.getFile() == null || exchangeEditor.getFile().isEmpty()) {
                throw new FileNotFoundException("No uploaded file found");
            } else {
                try (InputStream exchangePackageStream = exchangeEditor.getFile().getInputStream()) {
                    String exchangeFileName = exchangeEditor.getFile().getOriginalFilename();
                    int exchangeFileTypeSeparatorIndex = exchangeFileName.lastIndexOf(".");
                    String exchangeFileExtension = exchangeFileTypeSeparatorIndex <= 0 ? "XML" : exchangeFileName.substring(exchangeFileTypeSeparatorIndex + 1);
                    FlightsExchangeFormat exchangeFormat = FlightsExchangeFormat.valueOf(exchangeFileExtension.toUpperCase());
                    FlightsExchangeHandler exchangeHandler = exchangeFormat.getHandler();
                    FlightsExchangePackage exchangePackage = exchangeHandler.importPackage(exchangePackageStream);
                    exchangeEditor.setExchangePackage(exchangePackage);
                    return "flights/import/verify";
                }
            }
        } catch (Exception e) {
            model.addAttribute("importException", e);
            return this.doImportFileGet();
        }
    }

    @PostMapping("/import/verify")
    String doImportVerifyPost(@ModelAttribute("exchangeEditor") FlightsExchangeEditor exchangeEditor) {
        this.getExchangeService().importPackage(exchangeEditor.getExchangePackage(), this.getUserHolder().getCurrentUser());
        return "flights/import/done";
    }

    @RequestMapping("/export/{format}")
    ResponseEntity<?> doExport(@PathVariable("format") String formatValue) throws IOException  {
        FlightsExchangeFormat exchangeFormat = FlightsExchangeFormat.valueOf(formatValue.toUpperCase());
        FlightsExchangePackage exchangePackage = this.getExchangeService().createPackage(this.getUserHolder().getCurrentUser());
        try (ByteArrayOutputStream exchangeStream = new ByteArrayOutputStream()) {

            exchangeFormat.getHandler().exportPackage(exchangePackage, exchangeStream);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssX");
            String dateTime = dateTimeFormatter.format(exchangePackage.getCreationTime().atZone(ZoneId.of("UTC")));
            StringBuilder exchangeFilename = new StringBuilder();
            exchangeFilename.append("flightlog-").append(dateTime);
            exchangeFilename.append(".").append(exchangeFormat.name().toLowerCase());

            return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=" + exchangeFilename.toString())
                .contentType(exchangeFormat.getMimeType())
                .body(exchangeStream.toByteArray());

        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }

    @ModelAttribute("exchangeEditor")
    FlightsExchangeEditor exchangeEditor() {
        return new FlightsExchangeEditor();
    }

    FlightsExchangeService getExchangeService() {
        return this.exchangeService;
    }
    @Autowired
    void setExchangeService(FlightsExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
