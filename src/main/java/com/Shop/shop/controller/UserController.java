package com.Shop.shop.controller;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.command.LoginRequest;
import com.Shop.shop.command.UpdateUserCommand;
import com.Shop.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid AddUserCommand addUserCommand) {
        userService.signup(addUserCommand);
        return ResponseEntity.status(HttpStatus.OK).body("User successfully added");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String login = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    @PutMapping("/account/{username}")
    public ResponseEntity<String> modifyUser(@RequestBody(required = false) @Valid UpdateUserCommand updateUserCommand,
                                             @PathVariable String username) {
        userService.modifyUser(username, updateUserCommand);
        return ResponseEntity.status(HttpStatus.OK).body("User successfully modified");
    }



}

