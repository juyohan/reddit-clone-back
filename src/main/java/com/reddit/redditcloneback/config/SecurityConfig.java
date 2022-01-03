package com.reddit.redditcloneback.config;

import com.reddit.redditcloneback.jwt.JwtAccessDeniedHandler;
import com.reddit.redditcloneback.jwt.JwtAuthenticationEntryPoint;
import com.reddit.redditcloneback.jwt.JwtFilter;
import com.reddit.redditcloneback.jwt.JwtProvider;
import com.reddit.redditcloneback.oauth.handler.OAuth2FailureHandler;
import com.reddit.redditcloneback.oauth.handler.OAuth2SuccessHandler;
import com.reddit.redditcloneback.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// csrf를 사용하지 않게 함.

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers().frameOptions().sameOrigin() // 동일 도메인 내에서는 iframe 접근이 가능하게 함.

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않게 설정함.

                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll() // 로그인, 회원가입
                .antMatchers(
                        "/api/feed/**",
                        "/api/like/**",
                        "/api/comment/**"
                ).permitAll()
//                .hasAnyRole("USER")
                .anyRequest()
                .authenticated()

                .and()
                .logout().clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)

                .and()
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
