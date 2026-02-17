package com.dembasiby.notificationservice.controller;

import com.dembasiby.notificationservice.config.JwtAuthenticationFilter;
import com.dembasiby.notificationservice.config.JwtService;
import com.dembasiby.notificationservice.config.SecurityConfig;
import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;
import com.dembasiby.notificationservice.model.NotificationChannel;
import com.dembasiby.notificationservice.model.NotificationStatus;
import com.dembasiby.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private NotificationService notificationService;

    private UsernamePasswordAuthenticationToken authToken() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void sendNotification_returns201() throws Exception {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .build();

        NotificationDto response = NotificationDto.builder()
                .id(1L)
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .status(NotificationStatus.SENT)
                .build();

        when(notificationService.sendNotification(any(SendNotificationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    void sendNotification_returns403WhenUnauthenticated() throws Exception {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("user-1")
                .recipientEmail("user@example.com")
                .subject("Test")
                .body("Body")
                .eventType("ORDER_CONFIRMED")
                .build();

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void sendNotification_returns400ForInvalidInput() throws Exception {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("")
                .recipientEmail("invalid-email")
                .subject("")
                .body("")
                .eventType("")
                .build();

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNotificationById_returns200() throws Exception {
        NotificationDto response = NotificationDto.builder()
                .id(1L)
                .userId("1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.SENT)
                .build();

        when(notificationService.getNotificationById(1L)).thenReturn(response);

        mockMvc.perform(get("/1")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("1"));
    }

    @Test
    void getMyNotifications_returns200() throws Exception {
        NotificationDto response = NotificationDto.builder()
                .id(1L)
                .userId("1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.SENT)
                .build();

        when(notificationService.getNotificationsByUserId("1")).thenReturn(List.of(response));

        mockMvc.perform(get("/my")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("1"));
    }
}
