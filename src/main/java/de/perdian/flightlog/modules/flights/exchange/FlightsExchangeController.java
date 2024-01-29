package de.perdian.flightlog.modules.flights.exchange;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flights/")
class FlightsExchangeController {

    @RequestMapping("/import/file")
    String doImportFileGet() {
        return "/flights/import";
    }

    @PostMapping(value = "/import/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String doImportFilePost(@ModelAttribute("exchangeEditor") FlightsExchangeEditor exchangeEditor, Model model) {
        try {
            FlightsExchangeHandler exchangeHandler = exchangeEditor.getFileFormat().getHandler();
            FlightsExchangePackage exchangePackage = exchangeHandler.importPackage(exchangeEditor);
            model.addAttribute("exchangePackage", exchangePackage);
            return "/flights/import/verify";
        } catch (Exception e) {
            model.addAttribute("importException", e);
            return this.doImportFileGet();
        }
    }

    @RequestMapping("/import/file/execute")
    String doImportFileExecute(@ModelAttribute("exchangePackage") FlightsExchangePackage exchangePackage) {
        throw new UnsupportedOperationException();
    }

    @ModelAttribute("exchangeEditor")
    FlightsExchangeEditor exchangeEditor() {
        return new FlightsExchangeEditor();
    }

    @RequestMapping("/export/{format}")
    String doExport(@PathVariable("format") FlightsExchangeFormat format) {
        throw new UnsupportedOperationException();
    }

}
