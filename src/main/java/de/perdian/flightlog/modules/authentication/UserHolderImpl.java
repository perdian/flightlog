package de.perdian.flightlog.modules.authentication;

import org.springframework.stereotype.Component;

@Component
class UserHolderImpl implements UserHolder {

    @Override
    public User getCurrentUser() {
        return null;
    }

}
