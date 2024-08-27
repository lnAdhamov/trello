package uz.pdp.trello.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationErrorHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{
        String errorMessage = "Invalid phone number or password";
        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Your account has been disabled";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "Your account has expired";
        }

        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}
