package com.example.h2_restful.service.user.magicLink;

import com.example.h2_restful.entity.user.User;

public interface MagicLinkService {

    public void createFirstAccessMagicLink(User user);
    public void createForgotPasswordMagicLink(User user);
}
