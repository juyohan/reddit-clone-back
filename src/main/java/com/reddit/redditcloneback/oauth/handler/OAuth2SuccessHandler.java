package com.reddit.redditcloneback.oauth.handler;

import com.google.gson.Gson;
import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        clearAuthenticationAttributes(request);

        UserDto.Response.LoginDetail loginDetail = jwtProvider.create(authentication);

        responseData(loginDetail, response);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return;
        }
        httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void responseData(
            UserDto.Response.LoginDetail loginDetail ,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String resData = new Gson().toJson(loginDetail);
        response.getWriter().write(resData);
    }
}
