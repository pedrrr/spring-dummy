package com.example.h2_restful.service.user.ott;

import com.example.h2_restful.entity.user.OneTimeToken;

import java.util.Optional;

public interface OneTimeTokenService {

    Optional<OneTimeToken> findByToken(String token);
    void save(OneTimeToken oneTimeToken);
    void delete(OneTimeToken oneTimeToken);
}
