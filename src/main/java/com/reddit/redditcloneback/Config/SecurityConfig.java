package com.reddit.redditcloneback.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/get/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self"))
                .frameOptions()
                .disable();


    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
