package de.perdian.flightlog.modules.authentication.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
class RegistrationServiceImpl implements RegistrationService {

    private boolean allowByDefault = false;
    private List<String> emailAddressesAllowlist = null;
    private List<String> emailAddressesBlocklist = null;

    @Override
    public boolean checkEmailAddressRegistrationAllowed(String emailAddress) {
        if (this.getEmailAddressesAllowlist().contains(emailAddress.toLowerCase(Locale.ROOT))) {
            return true;
        } else if (this.getEmailAddressesBlocklist().contains(emailAddress.toLowerCase(Locale.ROOT))) {
            return false;
        } else {
            return this.isAllowByDefault();
        }
    }

    public boolean isAllowByDefault() {
        return this.allowByDefault;
    }
    @Value("${flightlog.registration.allowByDefault:false}")
    public void setAllowByDefault(boolean allowByDefault) {
        this.allowByDefault = allowByDefault;
    }

    @Value("${flightlog.registration.emailAddressesAllowlist:}")
    void configureEmailAddressesAllowlist(String emailAddresses) {
        this.setEmailAddressesAllowlist(
            Stream.of(StringUtils.split(emailAddresses, ",;"))
                .map(StringUtils::strip)
                .map(StringUtils::toRootLowerCase)
                .filter(StringUtils::isNotEmpty)
                .toList()
        );
    }
    private List<String> getEmailAddressesAllowlist() {
        return this.emailAddressesAllowlist;
    }
    private void setEmailAddressesAllowlist(List<String> emailAddressesAllowlist) {
        this.emailAddressesAllowlist = emailAddressesAllowlist;
    }

    @Value("${flightlog.registration.emailAddressesBlocklist:}")
    void configureEmailAddressesBlocklist(String emailAddresses) {
        this.setEmailAddressesBlocklist(
            Stream.of(StringUtils.split(emailAddresses, ",;"))
                    .map(StringUtils::strip)
                    .map(StringUtils::toRootLowerCase)
                    .filter(StringUtils::isNotEmpty)
                    .toList()
        );
    }
    private List<String> getEmailAddressesBlocklist() {
        return this.emailAddressesBlocklist;
    }
    private void setEmailAddressesBlocklist(List<String> emailAddressesBlocklist) {
        this.emailAddressesBlocklist = emailAddressesBlocklist;
    }

}
