package com.metehan.config;

import com.metehan.authenticationApi.token.Token;
import com.metehan.authenticationApi.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Token token = tokenRepository.findByToken(
                Arrays.stream(request.getCookies()).map(cookie ->
                    cookie.getName().equals("token") ? cookie: null
                ).findFirst().orElse(null).getValue()
        ).orElse(null);

        token.setExpired(true);
        token.setRevoked(true);

        tokenRepository.save(token);
        System.out.println("end of the logout handler!!!!");
    }
}
