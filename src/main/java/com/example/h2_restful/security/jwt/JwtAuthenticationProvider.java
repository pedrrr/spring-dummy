package com.example.h2_restful.security.jwt;

import com.example.h2_restful.entity.user.OneTimeToken;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    OneTimeTokenService oneTimeTokenService;
    JwtService jwtService;
    public JwtAuthenticationProvider(OneTimeTokenService oneTimeTokenService,
                                     JwtService jwtService) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = authentication.getCredentials().toString();
        // retrieves OneTimeToken from database based on token provided though a request parameter.
        Optional<OneTimeToken> ott = oneTimeTokenService.findByToken(token);
        if(ott.isEmpty() || !jwtService.isValid(token)){
            throw new RuntimeException("invalid token.");
        }

        // retrieves a user based on fetched ott.
        User user = ott.get().getUser();

        // deletes token from database after usage.
        oneTimeTokenService.delete(ott.get());

        return new JwtAuthenticationToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
