package com.Shop.shop.service;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.model.User;
import com.Shop.shop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public void addUser(AddUserCommand addUserCommand) {
        userRepository.save(userMapper.mapUser(addUserCommand));
    }

    public void modifyUser(String username, AddUserCommand addUserCommand) {
        User user = userRepository.findByUsername(username);
        Optional.ofNullable(user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));

        userRepository.save(userMapper.mapUserModify(addUserCommand, user));
    }
}
