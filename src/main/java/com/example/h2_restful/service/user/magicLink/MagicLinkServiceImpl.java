package com.example.h2_restful.service.user.magicLink;

import com.example.h2_restful.entity.user.OneTimeToken;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.jwt.JwtServiceImpl;
import com.example.h2_restful.service.user.notification.NotificationService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MagicLinkServiceImpl implements MagicLinkService {

    private final NotificationService notificationService;
    private final JwtServiceImpl jwtService;
    private final OneTimeTokenService oneTimeTokenService;
    public MagicLinkServiceImpl(NotificationService notificationService,
                                JwtServiceImpl jwtService,
                                OneTimeTokenService oneTimeTokenService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.oneTimeTokenService = oneTimeTokenService;
    }

    @Override
    public void createFirstAccessMagicLink(User user) {

        // creates a JWT token used for a first time log
        String token = jwtService.createFirstAccessToken(user.getEmail());
        this.handleMagicLink(user, token);
    }

    @Override
    public void createForgotPasswordMagicLink(User user) {
        // creates a JWT token used for a first time log
        String token = jwtService.createForgotPasswordToken(user.getEmail());
        this.handleMagicLink(user, token);
    }

    private void handleMagicLink(User user, String token){

        Date expirationDate = jwtService.extractExpirationDate(token);
        String userEmail = jwtService.extractSubject(token);

        OneTimeToken ott = new OneTimeToken(user, expirationDate, token);

        // creates Magic Link URL
        String url = "http://localhost:8080/auth/login-one-time?token=" + token;
        // sends Magic Link through NotificationService
        notificationService.sendMagicLinkNotification(userEmail, url);

        oneTimeTokenService.save(ott);
    }

    @Override
    public void authenticate(String token, HttpServletRequest request) {

        // todo: check if token isn't expired + add a way to differentiate registration tokens from 'forgot password' tokens
        // retrieves OneTimeToken from database based on token provided though a request parameter.
        Optional<OneTimeToken> ott = oneTimeTokenService.findByToken(token);
        if(ott.isEmpty() || !jwtService.isValid(token)){
            throw new RuntimeException("invalid token.");
        }

        // retrieves a user based on fetched ott.
        User user = ott.get().getUser();

        // authenticate user
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authToken);

        // saves the security context in the current session, allowing thymeleaf properly to access sec:authentication
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);


        // deletes ott from database
        oneTimeTokenService.delete(ott.get());
    }
}
