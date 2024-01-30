package de.perdian.flightlog.modules.flights.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Controller
@RequestMapping("/flights/")
class FlightsExchangeController {

    private static final Logger log = LoggerFactory.getLogger(FlightsExchangeController.class);

    @RequestMapping("/import/file")
    String doImportFileGet() {
        return "/flights/import";
    }

    @PostMapping(value = "/import/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String doImportFilePost(@ModelAttribute("exchangeEditor") FlightsExchangeEditor exchangeEditor, Model model) {
        try {
            if (exchangeEditor.getFile() == null || exchangeEditor.getFile().isEmpty()) {
                throw new FileNotFoundException("No uploaded file found");
            } else {
                try (InputStream exchangePackageStream = exchangeEditor.getFile().getInputStream()) {
                    FlightsExchangeHandler exchangeHandler = exchangeEditor.getExchangeFormat().getHandler();
                    FlightsExchangePackage exchangePackage = exchangeHandler.importPackage(exchangePackageStream);
                    exchangeEditor.setExchangePackage(exchangePackage);
                    return "/flights/import/verify";
                }
            }
        } catch (Exception e) {
            model.addAttribute("importException", e);
            return this.doImportFileGet();
        }
    }

    @PostMapping("/import/verify")
    String doImportVerifyPost(@ModelAttribute("exchangeEditor") FlightsExchangeEditor exchangeEditor) {
        log.error("IMPLEMENT DATABASE IMPORT!");
        return "/flights/import/done";
    }

    @RequestMapping("/export/{format}")
    String doExport(@PathVariable("format") FlightsExchangeFormat format) {
        throw new UnsupportedOperationException();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }

    @ModelAttribute("exchangeEditor")
    FlightsExchangeEditor exchangeEditor() {
        return new FlightsExchangeEditor();
    }

}
