package com.example.h2_restful.service.user.magicLink;

import com.example.h2_restful.entity.user.OneTimeToken;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.notification.NotificationService;
import com.example.h2_restful.service.user.ott.JwtService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MagicLinkServiceImpl implements MagicLinkService {

    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final OneTimeTokenService oneTimeTokenService;
    public MagicLinkServiceImpl(NotificationService notificationService,
                                JwtService jwtService,
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
}
