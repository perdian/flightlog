package de.perdian.flightlog.modules.authentication.service.registration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
class RegistrationServiceImpl implements RegistrationService {

    private boolean allowByDefault = false;
    private List<String> usernamesAllowlist = new ArrayList<>();
    private List<String> usernamesBlocklist = new ArrayList<>();

    @Override
    public boolean checkUsernameRegistrationAllowed(String username) {
        if (this.getUsernamesAllowlist().contains(username.toLowerCase(Locale.ROOT))) {
            return true;
        } else if (this.getUsernamesAllowlist().contains(username.toLowerCase(Locale.ROOT))) {
            return false;
        } else {
            return this.isAllowByDefault();
        }
    }

    public boolean isAllowByDefault() {
        return this.allowByDefault;
    }
    @Value("${FLIGHTLOG_REGISTRATION_ALLOW_BY_DEFAULT:false}")
    public void setAllowByDefault(boolean allowByDefault) {
        this.allowByDefault = allowByDefault;
    }

    @Value("${FLIGHTLOG_REGISTRATION_EMAIL_ADDRESSES_ALLOWLIST:}")
    void configureEmailAddressesAllowlist(String emailAddresses) {
        this.getUsernamesAllowlist().addAll(
            Stream.of(StringUtils.split(emailAddresses, ",;"))
                .map(StringUtils::strip)
                .map(StringUtils::toRootLowerCase)
                .filter(StringUtils::isNotEmpty)
                .toList()
        );
    }

    @Value("${FLIGHTLOG_REGISTRATION_USERNAMES_ALLOWLIST:}")
    void configureUsernamesAllowlist(String usernames) {
        this.getUsernamesAllowlist().addAll(
            Stream.of(StringUtils.split(usernames, ",;"))
                .map(StringUtils::strip)
                .map(StringUtils::toRootLowerCase)
                .filter(StringUtils::isNotEmpty)
                .toList()
        );
    }

    List<String> getUsernamesAllowlist() {
        return this.usernamesAllowlist;
    }
    void setUsernamesAllowlist(List<String> usernamesAllowlist) {
        this.usernamesAllowlist = usernamesAllowlist;
    }

    @Value("${FLIGHTLOG_REGISTRATION_EMAIL_ADDRESSES_BLOCKLIST:}")
    void configureEmailAddressesBlocklist(String emailAddresses) {
        this.getUsernamesBlocklist().addAll(
            Stream.of(StringUtils.split(emailAddresses, ",;"))
                .map(StringUtils::strip)
                .map(StringUtils::toRootLowerCase)
                .filter(StringUtils::isNotEmpty)
                .toList()
        );
    }

    @Value("${FLIGHTLOG_REGISTRATION_USERNAMES_BLOCKLIST:}")
    void configureUsernamesBlocklist(String usernames) {
        this.getUsernamesBlocklist().addAll(
            Stream.of(StringUtils.split(usernames, ",;"))
                .map(StringUtils::strip)
                .map(StringUtils::toRootLowerCase)
                .filter(StringUtils::isNotEmpty)
                .toList()
        );
    }

    List<String> getUsernamesBlocklist() {
        return this.usernamesBlocklist;
    }
    void setUsernamesBlocklist(List<String> usernamesBlocklist) {
        this.usernamesBlocklist = usernamesBlocklist;
    }

}
