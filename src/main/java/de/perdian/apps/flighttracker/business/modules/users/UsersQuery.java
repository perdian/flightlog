package de.perdian.apps.flighttracker.business.modules.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public class UsersQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Collection<Long> restrictUserIdentifiers = null;
    private Collection<String> restrictUsernames = null;
    private Collection<String> restrictAuthenticationSources = null;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("restrictUserIdentifiers", this.getRestrictUserIdentifiers());
        toStringBuilder.append("restrictUsernames", this.getRestrictUsernames());
        toStringBuilder.append("restrictAuthenticationSources", this.getRestrictAuthenticationSources());
        return toStringBuilder.toString();
    }

    Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();
        if (this.getRestrictAuthenticationSources() != null && !this.getRestrictAuthenticationSources().isEmpty()) {
            predicateList.add(root.get("authenticationSource").in(this.getRestrictAuthenticationSources()));
        }
        if (this.getRestrictUserIdentifiers() != null && !this.getRestrictUserIdentifiers().isEmpty()) {
            predicateList.add(root.get("userId").in(this.getRestrictUserIdentifiers()));
        }
        if (this.getRestrictUsernames() != null && !this.getRestrictUsernames().isEmpty()) {
            predicateList.add(root.get("username").in(this.getRestrictUsernames()));
        }
        return cb.and(predicateList.toArray(new Predicate[0]));
    }

    public Collection<Long> getRestrictUserIdentifiers() {
        return this.restrictUserIdentifiers;
    }
    public void setRestrictUserIdentifiers(Collection<Long> restrictUserIdentifiers) {
        this.restrictUserIdentifiers = restrictUserIdentifiers;
    }

    public Collection<String> getRestrictUsernames() {
        return this.restrictUsernames;
    }
    public void setRestrictUsernames(Collection<String> restrictUsernames) {
        this.restrictUsernames = restrictUsernames;
    }

    public Collection<String> getRestrictAuthenticationSources() {
        return this.restrictAuthenticationSources;
    }
    public void setRestrictAuthenticationSources(Collection<String> restrictAuthenticationSources) {
        this.restrictAuthenticationSources = restrictAuthenticationSources;
    }

}
