package de.perdian.flightlog.modules.authentication.configuration.ldap;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetailsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

class LdapAuthenticationUserDetailsContextMapper implements UserDetailsContextMapper {

    private FlightlogUserDetailsResolver flightlogUserDetailsResolver = null;

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        InetOrgPersonContextMapper inetOrgPersonContextMapper = new InetOrgPersonContextMapper();
        InetOrgPerson inetOrgPerson = (InetOrgPerson)inetOrgPersonContextMapper.mapUserFromContext(ctx, username, authorities);
        return this.getFlightlogUserDetailsResolver().resolveUserDetails(inetOrgPerson.getUid(), "LDAP");
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
    }

    FlightlogUserDetailsResolver getFlightlogUserDetailsResolver() {
        return this.flightlogUserDetailsResolver;
    }
    @Autowired
    void setFlightlogUserDetailsResolver(FlightlogUserDetailsResolver flightlogUserDetailsResolver) {
        this.flightlogUserDetailsResolver = flightlogUserDetailsResolver;
    }

}
