package ru.kata.spring.rest.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public LoginAuthenticationFailureHandler() {
        super("/?error");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (isAllowSessionCreation()) {
            request.getSession().setAttribute("authException", exception.getMessage());
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}