package com.example.h2_restful.controller;

import com.example.h2_restful.entity.user.OneTimeToken;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.UserService;
import com.example.h2_restful.service.user.ott.JwtService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LogController {

    OneTimeTokenService oneTimeTokenService;
    JwtService jwtService;
    UserService userService;
    public LogController(OneTimeTokenService oneTimeTokenService,
                         JwtService jwtService,
                         UserService userService) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {

        return "login-form";
    }

    @GetMapping("/auth/login-one-time")
    public String magicLinkLogin(@RequestParam("token") String token) {

        // todo: check if token isn't expired + add a way to differentiate registration tokens from 'forgot password' tokens
        // retrieves OneTimeToken from database based on token provided though a request parameter.
        Optional<OneTimeToken> ott = oneTimeTokenService.findByToken(token);
        if(ott.isEmpty() || !jwtService.isValid(token)){
            throw new RuntimeException("invalid token.");
        }

        // retrieves a user based on fetched ott.
        User user = ott.get().getUser();

        // authenticate user
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // deletes ott from database
        oneTimeTokenService.delete(ott.get());

        return "redirect:/user";
    }

}
