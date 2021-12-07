package com.reddit.redditcloneback.Handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // 어떤 클래스를 상속받았는지, 어떤 클래스인지 확인하는 연산자이다.
        if (exception instanceof BadCredentialsException) {
            System.out.println("실패했음");
        } else if (exception instanceof InternalAuthenticationServiceException) {
            System.out.println("연동이 안됨");
        } else if (exception instanceof UsernameNotFoundException) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("kakaoId", exception.getMessage());

            Cookie cookie = new Cookie("kakaoId", exception.getMessage());
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);
            System.out.println("httpSession.getAttribute(\"kakaoId\") = " + httpSession.getAttribute("kakaoId"));
            redirectStrategy.sendRedirect(request, response, "http://localhost:3000/connect");
        }
    }
}
