/*
 * Flightlog (https://github.com/perdian/flightlog)
 * Copyright 2017-2022 Christian Seifert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.perdian.flightlog.configuration.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
class FlightlogWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private FlightlogAuthenticationSettings settings = null;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (this.getSettings().isRequired()) {
//            if (this.getSettings().getLocal().isEnabled() || this.getSettings().getLdap().isEnabled()) {
//                http.authenticationProvider(this.localAuthenticationProvider());
//            }
//            if (this.getSettings().getOauth().isEnabled()) {
//                http.oauth2Login().loginPage("/login").userInfoEndpoint().oidcUserService(this.oauthUserService());
//            }
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
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }
    }

//    @Bean
//    OAuth2UserService oauthUserService() {
//        return new OAuth2UserService();
//    }
//
//    @Bean
//    LocalAuthenticationProvider localAuthenticationProvider() {
//        return new LocalAuthenticationProvider();
//    }

    FlightlogAuthenticationSettings getSettings() {
        return this.settings;
    }
    @Autowired
    void setSettings(FlightlogAuthenticationSettings settings) {
        this.settings = settings;
    }

}
