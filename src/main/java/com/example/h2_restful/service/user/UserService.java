package com.example.h2_restful.service.user;

import com.example.h2_restful.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void deleteById(Long id);

    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsernameIgnoreCase(String username);
}
