package com.Shop.shop.serviceUser;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    User mapUser(AddUserCommand addUserCommand) {
        return User.builder()
                .name(addUserCommand.getName())
                .lastname(addUserCommand.getLastname())
                .username(addUserCommand.getUsername())
                .email(addUserCommand.getEmail())
                .phoneNumber(addUserCommand.getPhoneNumber())
                .password(addUserCommand.getPassword())
                .build();
    }
}
