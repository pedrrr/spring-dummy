package com.example.h2_restful.controller;

import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.entity.user.UserDTO;
import com.example.h2_restful.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    UserService userService;
    PasswordEncoder passwordEncoder;
    public SettingsController(UserService userService,
                              PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/update-password")
    public String updatePassword(Model model) {

        model.addAttribute("user", new UserDTO());
        return "update-password-form";
    }

    @PostMapping("/update-password")
    public String updatePasswordConfirm(@ModelAttribute("userDTO") UserDTO userDTO, Principal principal) {

        String currentPasswordEntered = userDTO.currentPassword;
        String newPasswordEntered = userDTO.newPassword;

        String username = principal.getName();

        Optional<User> user = userService.findByUsernameOrEmail(username, username);
        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }

        if(!passwordEncoder.matches(currentPasswordEntered, user.get().getPassword())){
            return "redirect:/settings/update-password?error=current_password_not_match";
        } else {
            user.get().setPassword(passwordEncoder.encode(newPasswordEntered));
            userService.save(user.get());
            return "redirect:/me";
        }
    }
}
