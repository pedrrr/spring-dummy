package com.example.h2_restful.controller;

import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.security.jwt.JwtAuthenticationToken;
import com.example.h2_restful.security.jwt.JwtServiceImpl;
import com.example.h2_restful.service.user.UserService;
import com.example.h2_restful.service.user.magicLink.MagicLinkService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

        Authentication auth = new JwtAuthenticationToken(token);
        Authentication result = authenticationManager.authenticate(auth);

        // saves authentication on the session, allowing thymeleaf to access sec:authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(result);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // retrieves now authenticated user's username from spring security context
        context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        String authenticatedUsername = user.getUsername();

        // redirects user to its own profile page
        return "redirect:/" + authenticatedUsername;
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
