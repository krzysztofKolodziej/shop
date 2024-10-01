package com.Shop.shop.service.emailService;

import com.Shop.shop.model.User;
import com.Shop.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {
    private UserRepository userRepository;

    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

}
