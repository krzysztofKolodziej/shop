package com.Shop.shop.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String usernameOrPassword;
    private String password;

}