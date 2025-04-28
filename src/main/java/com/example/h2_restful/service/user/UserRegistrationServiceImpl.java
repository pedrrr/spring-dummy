package com.example.h2_restful.service.user;

import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.magicLink.MagicLinkService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MagicLinkService magicLinkService;

    public UserRegistrationServiceImpl(UserService userService,
                                       PasswordEncoder passwordEncoder,
                                       MagicLinkService magicLinkService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.magicLinkService = magicLinkService;
    }

    @Override
    public void register(User user) {

        user.setEnabled(true);

        // gets user password submitted by the form and hashes it using BCrypt before
        // saving to database.
        String plainPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        user.setPassword(hashedPassword);
        userService.save(user);

        magicLinkService.createFirstAccessMagicLink(user);
    }
}
