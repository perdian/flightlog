package de.perdian.apps.flighttracker.modules.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@EnableWebSecurity
public abstract class FlighttrackerSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

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
                .userDetailsService(this.flighttrackerSecurityService())
            .and().userDetailsService(this.flighttrackerSecurityService());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.flighttrackerSecurityService());
    }

    @Bean
    public FlighttrackerSecurityService flighttrackerSecurityService() {
        return this.createSecurityService();
    }

    protected abstract FlighttrackerSecurityService createSecurityService();

}
