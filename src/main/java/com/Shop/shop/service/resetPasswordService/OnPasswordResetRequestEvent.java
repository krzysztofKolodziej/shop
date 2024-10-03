package com.Shop.shop.service.resetPasswordService;

import com.Shop.shop.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnPasswordResetRequestEvent extends ApplicationEvent {
    private final User user;
    private final String token;

    public OnPasswordResetRequestEvent(User user, String token) {
        super(user);
        this.user = user;
        this.token = token;
    }
}
