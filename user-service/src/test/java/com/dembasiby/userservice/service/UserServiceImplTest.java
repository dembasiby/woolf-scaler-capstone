package com.dembasiby.userservice.service;

import com.dembasiby.userservice.dto.request.*;
import com.dembasiby.userservice.dto.response.AuthResponse;
import com.dembasiby.userservice.dto.response.UserDto;
import com.dembasiby.userservice.exception.*;
import com.dembasiby.userservice.model.AppUser;
import com.dembasiby.userservice.model.PasswordResetToken;
import com.dembasiby.userservice.model.Role;
import com.dembasiby.userservice.repository.AppUserRepository;
import com.dembasiby.userservice.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = AppUser.builder()
                .id(1L)
                .email("test@example.com")
                .password("encoded-password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void register_shouldCreateUserAndReturnAuthResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .email("new@example.com")
                .password("password123")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        when(appUserRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken(anyLong(), anyString())).thenReturn("jwt-token");

        AuthResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("new@example.com", response.getUser().getEmail());
        verify(userEventProducer).publishUserEvent(any());
    }

    @Test
    void register_shouldThrowWhenEmailExists() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        when(appUserRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    void login_shouldReturnAuthResponseForValidCredentials() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        when(appUserRepository.findByEmailAndIsDeletedFalse("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken(1L, "test@example.com")).thenReturn("jwt-token");

        AuthResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_shouldThrowForInvalidEmail() {
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        when(appUserRepository.findByEmailAndIsDeletedFalse("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void login_shouldThrowForWrongPassword() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("wrong-password")
                .build();

        when(appUserRepository.findByEmailAndIsDeletedFalse("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void getCurrentUser_shouldReturnUserDto() {
        when(appUserRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));

        UserDto dto = userService.getCurrentUser(1L);

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
    }

    @Test
    void getCurrentUser_shouldThrowWhenNotFound() {
        when(appUserRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getCurrentUser(99L));
    }

    @Test
    void updateProfile_shouldUpdateFields() {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .firstName("Updated")
                .phoneNumber("1234567890")
                .build();

        when(appUserRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        UserDto dto = userService.updateProfile(1L, request);

        assertEquals("Updated", testUser.getFirstName());
        assertEquals("1234567890", testUser.getPhoneNumber());
        verify(userEventProducer).publishUserEvent(any());
    }

    @Test
    void deleteUser_shouldSoftDelete() {
        when(appUserRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        userService.deleteUser(1L);

        assertTrue(testUser.getIsDeleted());
        verify(userEventProducer).publishUserEvent(any());
    }

    @Test
    void getUserById_shouldReturnUserDto() {
        when(appUserRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));

        UserDto dto = userService.getUserById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void requestPasswordReset_shouldCreateTokenAndPublishEvent() {
        PasswordResetRequestDto request = PasswordResetRequestDto.builder()
                .email("test@example.com")
                .build();

        when(appUserRepository.findByEmailAndIsDeletedFalse("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(i -> i.getArgument(0));

        userService.requestPasswordReset(request);

        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        verify(userEventProducer).publishPasswordResetEvent(any());
    }

    @Test
    void requestPasswordReset_shouldThrowWhenUserNotFound() {
        PasswordResetRequestDto request = PasswordResetRequestDto.builder()
                .email("nonexistent@example.com")
                .build();

        when(appUserRepository.findByEmailAndIsDeletedFalse("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.requestPasswordReset(request));
    }

    @Test
    void confirmPasswordReset_shouldResetPassword() {
        PasswordResetConfirmDto request = PasswordResetConfirmDto.builder()
                .token("valid-token")
                .newPassword("new-password123")
                .build();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token("valid-token")
                .userId(1L)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        when(passwordResetTokenRepository.findByTokenAndUsedFalse("valid-token")).thenReturn(Optional.of(resetToken));
        when(appUserRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("new-password123")).thenReturn("new-encoded-password");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        userService.confirmPasswordReset(request);

        assertEquals("new-encoded-password", testUser.getPassword());
        assertTrue(resetToken.getUsed());
        verify(passwordResetTokenRepository).save(resetToken);
    }

    @Test
    void confirmPasswordReset_shouldThrowForInvalidToken() {
        PasswordResetConfirmDto request = PasswordResetConfirmDto.builder()
                .token("invalid-token")
                .newPassword("new-password123")
                .build();

        when(passwordResetTokenRepository.findByTokenAndUsedFalse("invalid-token")).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> userService.confirmPasswordReset(request));
    }

    @Test
    void confirmPasswordReset_shouldThrowForExpiredToken() {
        PasswordResetConfirmDto request = PasswordResetConfirmDto.builder()
                .token("expired-token")
                .newPassword("new-password123")
                .build();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token("expired-token")
                .userId(1L)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .used(false)
                .build();

        when(passwordResetTokenRepository.findByTokenAndUsedFalse("expired-token")).thenReturn(Optional.of(resetToken));

        assertThrows(InvalidTokenException.class, () -> userService.confirmPasswordReset(request));
    }
}
