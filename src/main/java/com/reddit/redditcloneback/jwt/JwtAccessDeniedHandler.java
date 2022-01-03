package com.reddit.redditcloneback.jwt;

import com.google.gson.Gson;
import com.reddit.redditcloneback.common.response.ErrorResponse;
import com.reddit.redditcloneback.exception.Exceptions;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.reddit.redditcloneback.exception.Exceptions.PERMISSION_DENIED;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse(PERMISSION_DENIED, request);

        String json = new Gson().toJson(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }
}
