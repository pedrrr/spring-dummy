package com.example.h2_restful.dao;

import com.example.h2_restful.entity.user.OneTimeToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneTimeTokenRepository extends JpaRepository<OneTimeToken, Long> {

    Optional<OneTimeToken> findByToken(String token);
}
