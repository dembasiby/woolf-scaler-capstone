package com.dembasiby.userservice.controller;

import com.dembasiby.userservice.config.JwtAuthenticationFilter;
import com.dembasiby.userservice.config.SecurityConfig;
import com.dembasiby.userservice.dto.request.LoginRequest;
import com.dembasiby.userservice.dto.request.RegisterRequest;
import com.dembasiby.userservice.dto.response.AuthResponse;
import com.dembasiby.userservice.dto.response.UserDto;
import com.dembasiby.userservice.model.Role;
import com.dembasiby.userservice.service.JwtService;
import com.dembasiby.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_shouldReturn201() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt-token")
                .user(userDto)
                .build();

        when(userService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void register_shouldReturn400ForInvalidInput() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("invalid-email")
                .password("short")
                .firstName("")
                .lastName("")
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn200() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt-token")
                .user(userDto)
                .build();

        when(userService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void getMe_shouldReturn401WithoutToken() throws Exception {
        mockMvc.perform(get("/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMe_shouldReturn200WithValidToken() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        when(userService.getCurrentUser(1L)).thenReturn(userDto);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(1L, null, List.of());

        mockMvc.perform(get("/me")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserById_shouldReturn401WithoutToken() throws Exception {
        mockMvc.perform(get("/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_shouldReturn200WithValidToken() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        when(userService.getUserById(1L)).thenReturn(userDto);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(1L, null, List.of());

        mockMvc.perform(get("/1")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
