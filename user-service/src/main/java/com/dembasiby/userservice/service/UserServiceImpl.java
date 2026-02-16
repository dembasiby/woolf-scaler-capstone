package com.dembasiby.userservice.service;

import com.dembasiby.userservice.dto.request.*;
import com.dembasiby.userservice.dto.response.AuthResponse;
import com.dembasiby.userservice.dto.response.UserDto;
import com.dembasiby.userservice.dto.response.event.PasswordResetEvent;
import com.dembasiby.userservice.dto.response.event.UserEventType;
import com.dembasiby.userservice.exception.*;
import com.dembasiby.userservice.mapper.UserMapper;
import com.dembasiby.userservice.model.AppUser;
import com.dembasiby.userservice.model.PasswordResetToken;
import com.dembasiby.userservice.repository.AppUserRepository;
import com.dembasiby.userservice.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserEventProducer userEventProducer;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        AppUser user = UserMapper.toAppUser(request, encodedPassword);
        AppUser savedUser = appUserRepository.save(user);

        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());

        userEventProducer.publishUserEvent(UserMapper.toUserEvent(savedUser, UserEventType.USER_REGISTERED));

        return AuthResponse.builder()
                .token(token)
                .user(UserMapper.toUserDto(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .user(UserMapper.toUserDto(user))
                .build();
    }

    @Override
    public UserDto getCurrentUser(Long userId) {
        AppUser user = findActiveUserById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateProfile(Long userId, UpdateProfileRequest request) {
        AppUser user = findActiveUserById(userId);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getSocialProfile() != null) user.setSocialProfile(request.getSocialProfile());

        AppUser updatedUser = appUserRepository.save(user);

        userEventProducer.publishUserEvent(UserMapper.toUserEvent(updatedUser, UserEventType.USER_UPDATED));

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        AppUser user = findActiveUserById(userId);
        user.setIsDeleted(true);
        appUserRepository.save(user);

        userEventProducer.publishUserEvent(UserMapper.toUserEvent(user, UserEventType.USER_DELETED));
    }

    @Override
    public UserDto getUserById(Long id) {
        AppUser user = findActiveUserById(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void requestPasswordReset(PasswordResetRequestDto request) {
        AppUser user = appUserRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        PasswordResetEvent event = PasswordResetEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .token(token)
                .email(user.getEmail())
                .userId(user.getId())
                .timestamp(LocalDateTime.now())
                .build();

        userEventProducer.publishPasswordResetEvent(event);
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirmDto request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token has expired");
        }

        AppUser user = findActiveUserById(resetToken.getUserId());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        appUserRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private AppUser findActiveUserById(Long id) {
        return appUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}
