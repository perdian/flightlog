package de.perdian.flightlog.modules.authentication.configuration.oauth2;

import de.perdian.flightlog.modules.authentication.exceptions.RegistrationRestrictedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ConditionalOnBean(Oauth2AuthenticationConfiguration.class)
class Oauth2AuthenticationLoginController {

    private static final Logger log = LoggerFactory.getLogger(Oauth2AuthenticationLoginController.class);

    @RequestMapping("/authentication/login/oauth2")
    String doLogin(Model model, HttpServletRequest servletRequest) {

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
            log.debug("Exception occurred during login", authenticationException);
            httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            return "/authentication/login/oauth-error";
        } else {
            return "redirect:/oauth2/authorization/google";
        }

    }

}
