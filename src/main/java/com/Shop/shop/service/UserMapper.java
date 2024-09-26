package com.Shop.shop.service;

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

    User mapUserModify(AddUserCommand addUserCommand, User user) {
        user.setName(addUserCommand.getName());
        user.setLastname(user.getLastname());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPhoneNumber(addUserCommand.getPhoneNumber());
        user.setPhoneNumber(addUserCommand.getPhoneNumber());
        return user;
    }
}
