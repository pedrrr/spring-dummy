package com.example.h2_restful.security.jwt;

import java.util.Date;

public interface JwtService {

    String createFirstAccessToken(String subject);
    String createForgotPasswordToken(String subject);
    String extractSubject(String token);
    Date extractExpirationDate(String token);
    boolean isValid(String token);
    boolean isExpired(String token);
}
