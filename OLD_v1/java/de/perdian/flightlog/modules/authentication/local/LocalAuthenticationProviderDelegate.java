package de.perdian.flightlog.modules.authentication.local;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

interface LocalAuthenticationProviderDelegate {

    UserEntity resolveUser(String username, String password);

}
