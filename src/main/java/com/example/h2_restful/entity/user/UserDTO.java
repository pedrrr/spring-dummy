package com.example.h2_restful.entity.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    public String currentPassword;
    public String newPassword;
}
