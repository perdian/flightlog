package de.perdian.flightlog.modules.errors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Model model) {
        Throwable exception = this.resolveException(servletRequest);
        log.error("Caught exception", exception);
        model.addAttribute("httpStatusCode", servletResponse.getStatus());
        model.addAttribute("exceptions", this.createExceptionWrappers(exception));
        return "/error";
    }

    private Throwable resolveException(HttpServletRequest servletRequest) {
        Exception requestDispatcherException = (Exception)servletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (requestDispatcherException instanceof ServletException servletException) {
            return servletException.getCause();
        } else if (requestDispatcherException != null) {
            return requestDispatcherException;
        } else {
            return (Exception) servletRequest.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        }
    }

    private List<ExceptionWrapper> createExceptionWrappers(Throwable exception) {
        List<ExceptionWrapper> exceptionWrappers = new ArrayList<>();
        for (Throwable e = exception ; e != null; e = e.getCause()) {
            exceptionWrappers.add(new ExceptionWrapper(e));
        }
        return exceptionWrappers;
    }

    public static class ExceptionWrapper implements Serializable {

        static final long serialVersionUID = 1L;

        private Throwable exception = null;
        private String stacktrace = null;

        private ExceptionWrapper(Throwable e) {
            this.setException(e);
            this.setStacktrace(ExceptionUtils.getStackTrace(e));
        }

        public String getMessage() {
            return this.getException().getMessage();
        }

        public Throwable getException() {
            return this.exception;
        }
        private void setException(Throwable exception) {
            this.exception = exception;
        }

        public String getStacktrace() {
            return this.stacktrace;
        }
        private void setStacktrace(String stacktrace) {
            this.stacktrace = stacktrace;
        }

    }

}
