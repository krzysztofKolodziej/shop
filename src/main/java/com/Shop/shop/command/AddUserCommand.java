package com.Shop.shop.command;

import com.Shop.shop.command.validation.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddUserCommand {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "LastName cannot be empty")
    private String lastname;
    @NotBlank(message = "User cannot be empty")
    private String username;
    @Email(message = "Email doesn't meet the requirements")
    @NotNull(message = "Email cannot be empty")
    private String email;
    @NotNull(message = "Phone number cannot be empty")
    @Size(min = 9, max = 9, message = "Password must be exactly 8 characters long")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;
    @ValidPassword
    private String password;
}
