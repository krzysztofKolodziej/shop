package com.Shop.shop.service;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.model.User;
import com.Shop.shop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public User addUser(AddUserCommand addUserCommand) {
        return userRepository.save(userMapper.mapUser(addUserCommand));
    }
}
