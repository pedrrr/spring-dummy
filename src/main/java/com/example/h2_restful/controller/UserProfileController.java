package com.example.h2_restful.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserProfileController {

    @RequestMapping("/me")
    public String profile() {
        return "user-profile";
    }
}
