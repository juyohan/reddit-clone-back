package com.reddit.redditcloneback.jwt;

import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtProvider jwtProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws IOException, ServletException {

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt, request)) {
            Authentication authentication = jwtProvider.parseJws(jwt, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {

        }

        filterchain.doFilter(request,response);
    }

    /**
     * 토큰의 정보를 헤더에 추출을 한 뒤, 알맞은 형태로 가공하는 함수이다.
     * @param request HttpServletRequest
     * @return jwt 를 반환한다.
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new BusinessException(Exceptions.NO_DATA);
    }
}
