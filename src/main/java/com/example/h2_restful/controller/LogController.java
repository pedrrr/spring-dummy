package com.example.h2_restful.controller;

import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.UserService;
import com.example.h2_restful.service.user.jwt.JwtServiceImpl;
import com.example.h2_restful.service.user.magicLink.MagicLinkService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LogController {

    OneTimeTokenService oneTimeTokenService;
    JwtServiceImpl jwtService;
    UserService userService;
    AuthenticationManager authenticationManager;
    MagicLinkService magicLinkService;
    public LogController(OneTimeTokenService oneTimeTokenService,
                         JwtServiceImpl jwtService,
                         UserService userService,
                         AuthenticationManager authenticationManager,
                         MagicLinkService magicLinkService) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.magicLinkService = magicLinkService;
    }

    @GetMapping("/login")
    public String login() {

        return "login-form";
    }

    @GetMapping("/auth/login-one-time")
    public String magicLinkLogin(@RequestParam("token") String token, HttpServletRequest request) {

        magicLinkService.authenticate(token, request);
        return "redirect:/me";
    }

    @PostMapping("/magic-linking")
    public String createMagicLink(@RequestParam("username") String username){

        Optional<User> user = userService.findByUsernameOrEmail(username, username);
        if(user.isEmpty()){
            throw new RuntimeException("User not found.");
        }

        magicLinkService.createForgotPasswordMagicLink(user.get()); // todo: a way to avoid multiple ott's sharing the same
                                                                    // todo: user to be saved (probably making a check on the database [if else])

        return "redirect:/magic-linking";
    }

    @GetMapping("/magic-linking")
    public String magicLinkCreated(){

        return "magic-linking";
    }


}
