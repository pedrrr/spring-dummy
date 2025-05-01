package com.example.h2_restful.controller;

import com.example.h2_restful.dao.UserRepository;
import com.example.h2_restful.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserProfileController {

    UserRepository userRepository;
    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model, Principal principal) {

        String normalizedUsername = username.toLowerCase();

        Optional<User> user = userRepository.findByUsernameIgnoreCase(normalizedUsername);
        if(user.isEmpty()) {
            return "redirect:/error/404";
        }

        // checks if user is accessing its own profile page
        if(principal != null && principal.getName().equalsIgnoreCase(username)) {
            model.addAttribute("ownProfile", true);
        }

        model.addAttribute("user", user.get());
        return "user-profile";
    }
}
