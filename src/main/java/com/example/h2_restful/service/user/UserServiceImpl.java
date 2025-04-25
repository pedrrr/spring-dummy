package com.example.h2_restful.service.user;

import com.example.h2_restful.dao.UserRepository;
import com.example.h2_restful.entity.user.User;
import com.example.h2_restful.service.user.ott.JwtService;
import com.example.h2_restful.service.user.ott.OneTimeTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OneTimeTokenService oneTimeTokenService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           OneTimeTokenService oneTimeTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.oneTimeTokenService = oneTimeTokenService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
