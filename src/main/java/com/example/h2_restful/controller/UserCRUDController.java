package com.example.h2_restful.controller;

import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.UserRegistrationService;
import com.example.h2_restful.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserCRUDController {

    private final UserService userService;
    private final UserRegistrationService userRegistrationService;
    public UserCRUDController(UserService userService,
                              UserRegistrationService userRegistrationService) {
        this.userService = userService;
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping
    public String getAllUsers(Model model) {

        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/register")
    public String createUser(Model model, @ModelAttribute User user) {

        model.addAttribute("user", new User());
        return "user-form";
    }
    @GetMapping("/update")
    public String updateUser(@RequestParam("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));
        return "user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {

        userRegistrationService.register(user);
        return "redirect:/user";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {

        userService.deleteById(id);
        return "redirect:/user";
    }


}
