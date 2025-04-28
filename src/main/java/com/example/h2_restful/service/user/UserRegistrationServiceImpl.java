package com.example.h2_restful.service.user;

import com.example.h2_restful.entity.user.OneTimeToken;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.notification.NotificationService;
import com.example.h2_restful.service.user.ott.JwtService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserService userService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OneTimeTokenService oneTimeTokenService;

    public UserRegistrationServiceImpl(UserService userService,
                           NotificationService notificationService,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           OneTimeTokenService oneTimeTokenService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.oneTimeTokenService = oneTimeTokenService;
    }

    @Override
    public void register(User user) {

        userService.save(user);

        // gets user password submitted by the form and hashes it using BCrypt before
        // saving to database.
        String plainPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        user.setPassword(hashedPassword);

        // creates a JWT token used for a first time log
        String token = jwtService.createFirstAccessToken(user.getEmail());

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
