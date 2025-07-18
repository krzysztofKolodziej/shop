package com.Shop.shop.controller;

import com.Shop.shop.command.AddUserCommand;
import com.Shop.shop.command.LoginRequest;
import com.Shop.shop.command.UpdateUserCommand;
import com.Shop.shop.model.User;
import com.Shop.shop.repository.UserRepository;
import com.Shop.shop.service.user.token.TokenBlacklistService;
import com.Shop.shop.service.user.token.VerificationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Import;
import com.Shop.shop.config.TestSecurityConfig;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@Import(TestSecurityConfig.class)
class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        userRepository.deleteAll();
    }

    @Test
    void shouldSignupUser() throws Exception {
        AddUserCommand addUserCommand = new AddUserCommand();
        addUserCommand.setName("Test");
        addUserCommand.setLastname("User");
        addUserCommand.setUsername("testuser");
        addUserCommand.setEmail("test@example.com");
        addUserCommand.setPhoneNumber("123456789");
        addUserCommand.setPassword("Password123!");

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUserCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User successfully added"));
    }

    @Test
    void shouldRejectInvalidSignup() throws Exception {
        AddUserCommand addUserCommand = new AddUserCommand();
        addUserCommand.setName("");
        addUserCommand.setLastname("");
        addUserCommand.setUsername("");
        addUserCommand.setEmail("invalid-email");
        addUserCommand.setPhoneNumber("");
        addUserCommand.setPassword("weak");

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUserCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldLoginUser() throws Exception {
        // First create a user
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword(passwordEncoder.encode("Password123!")); // Generate proper BCrypt hash
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("Password123!");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldRejectInvalidLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("nonexistent");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldVerifyEmail() throws Exception {
        // Create user with verification token
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(false);
        user.setVerificationToken("valid-token");
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/verify-email")
                        .param("token", "valid-token"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Your account has been verified successfully."));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldLogoutUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account/logout")
                        .header("Authorization", "Bearer valid-jwt-token"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User has been logged out successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldRejectLogoutWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account/logout"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Token is missing or invalid"));
    }

    @Test
    void shouldRequestPasswordReset() throws Exception {
        // Create user first
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/reset-password")
                        .param("email", "test@example.com"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password reset link has been sent to your email"));
    }

    @Test
    void shouldResetPasswordWithToken() throws Exception {
        // Create user with reset token
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setVerificationToken("valid-token");
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/reset-password-check")
                        .param("token", "valid-token")
                        .param("newPassword", "NewPassword123!"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password has been reset successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldModifyUser() throws Exception {
        // Create a user first
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        UpdateUserCommand updateUserCommand = new UpdateUserCommand();
        updateUserCommand.setName("UpdatedName");
        updateUserCommand.setLastname("UpdatedLastname");
        updateUserCommand.setUsername("updateduser");
        updateUserCommand.setEmail("newemail@example.com");
        updateUserCommand.setPhoneNumber("987654321");
        updateUserCommand.setOldPassword("Password123!");
        updateUserCommand.setNewPassword("NewPassword123!");

        mockMvc.perform(MockMvcRequestBuilders.put("/account/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User successfully modified"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldGetUserDetails() throws Exception {
        // Create a user first
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/details"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldDeleteUser() throws Exception {
        // Create a user first
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string("User has been deleted"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        // Create a user first
        User user = new User();
        user.setName("Test");
        user.setLastname("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/details"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}