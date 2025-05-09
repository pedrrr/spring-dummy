package com.example.h2_restful.config;

import com.example.h2_restful.security.jwt.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);

        // customizes login entry information based on users saved on the database
        userDetailsManager
                .setUsersByUsernameQuery
                ("SELECT username, password, enabled " +
                        "FROM users " +
                        "WHERE username=?");
        userDetailsManager
                .setAuthoritiesByUsernameQuery("SELECT username, role " +
                "FROM users " +
                "WHERE username=?");

        return userDetailsManager;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests( configurer -> configurer
                        .requestMatchers("/user/register",
                                "/auth/login-one-time",
                                "/magic-linking")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin( form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate-user")
                        .defaultSuccessUrl("/user", true)
                        .permitAll())
                .passwordManagement( management -> management
                        .changePasswordPage("/settings/update-password"))
                .logout( logout -> logout
                        .logoutUrl("/logout")
                        .permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       DaoAuthenticationProvider daoAuthProvider,
                                                       JwtAuthenticationProvider jwtAuthProvider) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(daoAuthProvider);
        authBuilder.authenticationProvider(jwtAuthProvider);
        return authBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 10 is the default strength
        return new BCryptPasswordEncoder(10);
    }
}
