package com.reddit.redditcloneback.Config;

import com.reddit.redditcloneback.JWT.JwtAccessDeniedHandler;
import com.reddit.redditcloneback.JWT.JwtAuthenticationEntryPoint;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.JWT.JwtSecurityConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final UserDetailsService userDetailsService;

//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    private final JwtProvider jwtProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// csrf를 사용하지 않게 함.

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않게 설정함.

                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/**").permitAll() // 로그인, 회원가입
                .antMatchers("/api/feed/**").permitAll() // 게시글
//                .antMatchers("/api/feed/**").authenticated()
                .antMatchers("/api/user/**").authenticated() // 개인 정보 페이지
                .antMatchers("/api/likes/**").permitAll() // 좋아요
                .antMatchers("/api/post/**").permitAll()
                .antMatchers("/api/files/**").permitAll()
                .antMatchers("/api/file/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .anyRequest()
                .authenticated()

//                .and()
//                .formLogin()
//                .loginPage("/api/")

                .and()
                .logout().clearAuthentication(true)
//                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

                .and()
                .apply(new JwtSecurityConfig(jwtProvider));

        // 자식 스레드와 부모 스레드에 관해 동일한 SecurityContext를 유지
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
