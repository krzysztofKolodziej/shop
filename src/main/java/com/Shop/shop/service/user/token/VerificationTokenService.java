package com.Shop.shop.service.user.token;

import com.Shop.shop.exception.InvalidTokenException;
import com.Shop.shop.model.User;
import com.Shop.shop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VerificationTokenService {

    private UserRepository userRepository;

    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
    }

    public void createPasswordResetToken(User user, String token) {
        user.setVerificationToken(token);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));
        
        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Verification token has expired");
        }
        
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

}
