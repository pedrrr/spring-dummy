package com.example.h2_restful.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {

    @GetMapping("/me")
    public String profile() {
        return "user-profile";
    }
}
