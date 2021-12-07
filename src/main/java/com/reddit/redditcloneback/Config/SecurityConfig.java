package com.reddit.redditcloneback.Config;

import com.reddit.redditcloneback.Handler.OAuth2FailureHandler;
import com.reddit.redditcloneback.Handler.OAuth2SuccessHandler;
import com.reddit.redditcloneback.JWT.JwtAccessDeniedHandler;
import com.reddit.redditcloneback.JWT.JwtAuthenticationEntryPoint;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.JWT.JwtSecurityConfig;
import com.reddit.redditcloneback.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
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
//
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/**").permitAll() // 로그인, 회원가입
                .antMatchers("/api/feed/**").permitAll() // 게시글
                .antMatchers("/api/user/**").authenticated() // 개인 정보 페이지
                .antMatchers("/api/likes/**").permitAll() // 좋아요
                .antMatchers("/api/post/**").permitAll()
                .antMatchers("/api/files/**").permitAll()
                .antMatchers("/api/file/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .logout().clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

                .and()
                .apply(new JwtSecurityConfig(jwtProvider));

        http
                .oauth2Login()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler);
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
