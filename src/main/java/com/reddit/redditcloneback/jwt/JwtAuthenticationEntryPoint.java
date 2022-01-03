package com.reddit.redditcloneback.jwt;

import com.google.gson.Gson;
import com.reddit.redditcloneback.common.response.ErrorResponse;
import com.reddit.redditcloneback.exception.Exceptions;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.reddit.redditcloneback.exception.Exceptions.*;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String exception = request.getAttribute("exception").toString();

        if (exception.equals(JWT_WRONG_TYPE_TOKEN.getCode()))
            setResponse(request, response, JWT_WRONG_TYPE_TOKEN);
        else if (exception.equals(JWT_EXPIRED_TOKEN.getCode()))
            setResponse(request, response, JWT_EXPIRED_TOKEN);
        else if (exception.equals(JWT_UNSUPPORTED_TOKEN.getCode()))
            setResponse(request, response, JWT_UNSUPPORTED_TOKEN);
        else if (exception.equals(JWT_WRONG_TOKEN.getCode()))
            setResponse(request, response, JWT_WRONG_TOKEN);
        else if (exception.equals(JWT_NULL_TOKEN.getCode()))
            setResponse(request, response, JWT_NULL_TOKEN);
        else
            setResponse(request, response, JWT_UNKNOWN_ERROR);

    }

    private void setResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            Exceptions exceptions
    ) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(exceptions, request);

        String json = new Gson().toJson(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }
}
