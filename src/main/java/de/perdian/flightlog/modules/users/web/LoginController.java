package de.perdian.flightlog.modules.users.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String doLogin() {
        return "/login/login";
    }

    @RequestMapping(value = "/logoutcomplete")
    public String doLogout() {
        return "/login/logout";
    }

}
