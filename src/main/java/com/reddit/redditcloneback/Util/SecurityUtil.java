package com.reddit.redditcloneback.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {

    }

    // Security Context에 유저의 인증정보가 있는지 확인하는 메소드
    public static Optional<String> getCurrentUsername() {
//        System.out.println("인증정보가 있는지 확인합니다요~");
        // JwtFilter 에서 doFilter 에서 저장되는 인증정보가 꺼내지게 되는 것이다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);

        if (authentication == null) {
            logger.debug("Security Context에 인증이 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        }
        else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}
