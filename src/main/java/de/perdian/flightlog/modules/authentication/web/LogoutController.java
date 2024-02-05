package de.perdian.flightlog.modules.authentication.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

    @RequestMapping("/authentication/logout/completed")
    String doLogoutCompleted(Model model) {
        return "authentication/logout/completed";
    }

}
