package com.reddit.redditcloneback.Handler;

import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.UserDTO.CustomOAuth2User;
import com.reddit.redditcloneback.JWT.JwtFilter;
import com.reddit.redditcloneback.JWT.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String DEFAULT_LOGIN_SUCCESS_URL = "http://localhost:3000";
    private final JwtProvider jwtProvider;

    public OAuth2SuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);

        Map<String, Object> properties = (Map<String, Object>) ((CustomOAuth2User) authentication.getPrincipal()).getAttributes().get("properties");
        String name = (String) properties.get("nickname");

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setUsername(((CustomOAuth2User) authentication.getPrincipal()).getName());
        jwtTokenDTO.setImageUrl(((CustomOAuth2User) authentication.getPrincipal()).getImageUrl());

        Cookie cookie = new Cookie("Access-Token", jwtProvider.createJws(authentication));
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);



        redirectStrategy(request, response);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return ;
        }
        httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void redirectStrategy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null)
            redirectStrategy.sendRedirect(request, response, DEFAULT_LOGIN_SUCCESS_URL);
    }
}
