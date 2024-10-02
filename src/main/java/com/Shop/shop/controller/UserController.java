package com.Shop.shop.controller;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.command.LoginRequest;
import com.Shop.shop.command.UpdateUserCommand;
import com.Shop.shop.model.User;
import com.Shop.shop.service.UserService;
import com.Shop.shop.service.emailService.OnRegistrationCompleteEvent;
import com.Shop.shop.service.emailService.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationEventPublisher;


@RestController
@AllArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid AddUserCommand addUserCommand) {
        User user = userService.signup(addUserCommand);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        return ResponseEntity.status(HttpStatus.OK).body("User successfully added");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String result = verificationTokenService.validateVerificationToken(token);
        if (result.equals("valid")) {
            return ResponseEntity.status(HttpStatus.FOUND).body("Your account has been verified successfully.");
        } else if (result.equals("expired")) {
            return ResponseEntity.status(HttpStatus.GONE).body("Verification token has been expired.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification token.");
        }
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

