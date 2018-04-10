package de.perdian.flightlog.modules.users.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.perdian.flightlog.modules.registration.exception.RegistrationRestrictedException;

@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String doLogin(HttpServletRequest servletRequest) {

        // If we do have an error resulting from the authentication then
        // remove it from the session and make it available in the request
        HttpSession httpSession = servletRequest.getSession(false);
        Exception authenticationException = httpSession == null ? null : (Exception)httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (httpSession != null && authenticationException != null) {
            if (authenticationException instanceof RegistrationRestrictedException) {
                servletRequest.setAttribute("registrationRestrictedException", authenticationException);
            } else {
                servletRequest.setAttribute("authenticationException", authenticationException);
            }
            httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        return "/login/login";
    }

    @RequestMapping(value = "/logoutcomplete")
    public String doLogout() {
        return "/login/logout";
    }

}
