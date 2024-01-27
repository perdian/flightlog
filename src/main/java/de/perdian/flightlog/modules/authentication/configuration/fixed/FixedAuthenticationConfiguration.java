package de.perdian.flightlog.modules.authentication.configuration.fixed;

import de.perdian.flightlog.modules.authentication.configuration.AbstractAuthenticationConfiguration;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.modules.authentication.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@ConditionalOnProperty(name = "flightlog.authentication.type", havingValue = "fixed")
public class FixedAuthenticationConfiguration extends AbstractAuthenticationConfiguration {

    private UserRepository userRepository = null;
    private String emailAddress = "example@example.com";

    @Override
    protected void configureSecurityFilterChainHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilter(this.fixedAuthenticationFilter());
    }

    @Bean
    FixedAuthenticationFilter fixedAuthenticationFilter() {

        Specification<UserEntity> entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("authenticationSource"), "fixed"),
            criteriaBuilder.equal(root.get("username"), this.getEmailAddress())
        );

        UserEntity entity = this.getUserRepository().findOne(entitySpecification).orElse(null);
        if (entity == null) {
            entity = new UserEntity();
            entity.setUsername(this.getEmailAddress());
            entity.setAuthenticationSource("fixed");
            entity = this.getUserRepository().save(entity);
        }
        return new FixedAuthenticationFilter(new FixedAuthenticationUser(entity));

    }

    String getEmailAddress() {
        return this.emailAddress;
    }
    void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
