package com.Shop.shop.service;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.command.UpdateUserCommand;
import com.Shop.shop.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    User mapUserModify(UpdateUserCommand updateUserCommand, User user) {
        user.setName(updateUserCommand.getName());
        user.setLastname(updateUserCommand.getLastname());
        user.setUsername(updateUserCommand.getUsername());
        user.setEmail(updateUserCommand.getEmail());
        user.setPhoneNumber(updateUserCommand.getPhoneNumber());
        if (updateUserCommand.getOldPassword().equals(updateUserCommand.getNewPassword())) {
            user.setPassword(updateUserCommand.getNewPassword());
        }
        return user;
    }
}
