package com.dembasiby.userservice.service;

import com.dembasiby.userservice.dto.request.*;
import com.dembasiby.userservice.dto.response.AuthResponse;
import com.dembasiby.userservice.dto.response.UserDto;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDto getCurrentUser(Long userId);
    UserDto updateProfile(Long userId, UpdateProfileRequest request);
    void deleteUser(Long userId);
    UserDto getUserById(Long id);
    void requestPasswordReset(PasswordResetRequestDto request);
    void confirmPasswordReset(PasswordResetConfirmDto request);
}
