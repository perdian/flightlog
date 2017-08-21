package de.perdian.apps.flighttracker.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class FlighttrackerSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private FlighttrackerAuthenticationProvider flighttrackerAuthenticationProvider = null;
    private FlighttrackerUserDetailsService flighttrackerUserDetailsService = null;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/logoutcomplete").permitAll()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logoutcomplete")
            .and().httpBasic()
            .and().rememberMe()
                .rememberMeCookieName("flighttracker")
                .alwaysRemember(true)
                .userDetailsService(this.getFlighttrackerUserDetailsService())
            .and().userDetailsService(this.getFlighttrackerUserDetailsService());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.getFlighttrackerAuthenticationProvider());
    }

    FlighttrackerAuthenticationProvider getFlighttrackerAuthenticationProvider() {
        return this.flighttrackerAuthenticationProvider;
    }
    @Autowired
    void setFlighttrackerAuthenticationProvider(FlighttrackerAuthenticationProvider flighttrackerAuthenticationProvider) {
        this.flighttrackerAuthenticationProvider = flighttrackerAuthenticationProvider;
    }

    FlighttrackerUserDetailsService getFlighttrackerUserDetailsService() {
        return this.flighttrackerUserDetailsService;
    }
    @Autowired
    void setFlighttrackerUserDetailsService(FlighttrackerUserDetailsService flighttrackerUserDetailsService) {
        this.flighttrackerUserDetailsService = flighttrackerUserDetailsService;
    }

}
