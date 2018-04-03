package de.perdian.flightlog.modules.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class FlightlogWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

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
                .rememberMeCookieName("flightlog")
                .alwaysRemember(true)
                .userDetailsService(this.flightlogSecurityService())
            .and().userDetailsService(this.flightlogSecurityService());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.flightlogSecurityService());
    }

    @Bean
    public AuthenticationProviderSkeleton flightlogSecurityService() {
        return this.createSecurityService();
    }

    protected abstract AuthenticationProviderSkeleton createSecurityService();

}
