package com.example.h2_restful.service.user.ott;

import com.example.h2_restful.dao.OneTimeTokenRepository;
import com.example.h2_restful.entity.user.OneTimeToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OneTimeTokenServiceImpl implements OneTimeTokenService {

    OneTimeTokenRepository oneTimeTokenRepository;
    public OneTimeTokenServiceImpl(OneTimeTokenRepository oneTimeTokenRepository) {
        this.oneTimeTokenRepository = oneTimeTokenRepository;
    }

    @Override
    public Optional<OneTimeToken> findByToken(String token) {

        return oneTimeTokenRepository.findByToken(token);
    }

    @Override
    public void save(OneTimeToken oneTimeToken) {
        oneTimeTokenRepository.save(oneTimeToken);
    }

    @Override
    public void delete(OneTimeToken oneTimeToken) {
        oneTimeTokenRepository.delete(oneTimeToken);
    }
}
