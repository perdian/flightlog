package de.perdian.apps.flighttracker.web.modules.importexport;

import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.perdian.apps.flighttracker.business.modules.importexport.ImportExportService;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataWriter;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.JsonDataWriter;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.OpenflightsCsvDataWriter;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.XmlDataWriter;

@Controller
public class ExportController {

    private ImportExportService importExportService = null;

    @RequestMapping(value = "/export/xml", produces = "application/xml")
    public void doXml(HttpServletResponse response) throws Exception {
        this.doExport(new XmlDataWriter(), "application/xml", ".xml", response);
    }

    @RequestMapping(value = "/export/json", produces = "application/json")
    public void doJson(HttpServletResponse response) throws Exception {
        this.doExport(new JsonDataWriter(), "application/json", ".json", response);
    }

    @RequestMapping(value = "/export/openflightscsv", produces = "text/csv")
    public void doOpenflightsCsv(HttpServletResponse response) throws Exception {
        this.doExport(new OpenflightsCsvDataWriter(), "text/csv", ".csv", response);
    }

    private void doExport(DataWriter<String> dataWriter, String contentType, String fileExtension, HttpServletResponse response) throws Exception {

        List<DataItem> dataItems = this.getImportExportService().exportDataItems();
        String dataString = dataWriter.writeDataItems(dataItems);
        byte[] dataBytes = dataString.getBytes("UTF-8");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss'Z'");
        StringBuilder fileName = new StringBuilder();
        fileName.append("flighttracker-backup-");
        fileName.append(dateTimeFormatter.format(Clock.systemUTC().instant().atZone(ZoneId.of("UTC"))));
        fileName.append(fileExtension);

        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("Content-Length", String.valueOf(dataBytes.length));
        response.getOutputStream().write(dataBytes);
        response.getOutputStream().flush();

    }

    ImportExportService getImportExportService() {
        return this.importExportService;
    }
    @Autowired
    void setImportExportService(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

}
