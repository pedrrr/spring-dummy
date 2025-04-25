package com.example.h2_restful.service.user.notification;

public interface NotificationService {

    public void sendMagicLinkNotification(String userEmail, String url);
}
