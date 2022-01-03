package com.reddit.redditcloneback.oauth.handler;

import com.google.gson.Gson;
import com.reddit.redditcloneback.common.response.ErrorResponse;
import com.reddit.redditcloneback.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class OAuth2FailureHandler
        implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2FailureHandler.class);

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        if (exception instanceof UsernameNotFoundException) {
            writeResponse(Exceptions.USER_NOT_FOUND, response, request);
        }
    }

    private void writeResponse(
            Exceptions exceptions,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(exceptions, request);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String str = new Gson().toJson(errorResponse);
        response.getWriter().write(str);
    }
}
