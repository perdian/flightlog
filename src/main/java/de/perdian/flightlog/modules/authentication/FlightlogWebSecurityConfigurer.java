package de.perdian.flightlog.modules.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.perdian.flightlog.modules.authentication.local.LocalAuthenticationProvider;
import de.perdian.flightlog.modules.authentication.oauth2.OAuth2UserService;

@Configuration
class FlightlogWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private FlightlogAuthenticationSettings settings = null;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (this.getSettings().isRequired()) {
            if (this.getSettings().getLocal().isEnabled() || this.getSettings().getLdap().isEnabled()) {
                http.authenticationProvider(this.localAuthenticationProvider());
            }
            if (this.getSettings().getOauth().isEnabled()) {
                http.oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                        .oidcUserService(this.oauthUserService());
            }
            http.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/logoutcomplete").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();
            http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/");
            http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logoutcomplete");

//                .and().rememberMe()
//                    .rememberMeCookieName("flightlog-rememberme")
//                    .alwaysRemember(true)
//                    .userDetailsService(username -> new User(username, "password", Collections.emptyList()));

        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }
    }

    @Bean
    OAuth2UserService oauthUserService() {
        return new OAuth2UserService();
    }

    @Bean
    LocalAuthenticationProvider localAuthenticationProvider() {
        return new LocalAuthenticationProvider();
    }

    FlightlogAuthenticationSettings getSettings() {
        return this.settings;
    }
    @Autowired
    void setSettings(FlightlogAuthenticationSettings settings) {
        this.settings = settings;
    }

}
