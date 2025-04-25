package com.example.h2_restful.service.user.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    //todo: implement a mailSender

    @Override
    public void sendMagicLinkNotification(String userEmail, String url){

        System.out.println(url);
    }
}
