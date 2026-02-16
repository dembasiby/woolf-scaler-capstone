package com.dembasiby.userservice.mapper;

import com.dembasiby.userservice.dto.request.RegisterRequest;
import com.dembasiby.userservice.dto.response.UserDto;
import com.dembasiby.userservice.dto.response.event.UserEvent;
import com.dembasiby.userservice.dto.response.event.UserEventType;
import com.dembasiby.userservice.model.AppUser;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserMapper {

    public static AppUser toAppUser(RegisterRequest request, String encodedPassword) {
        return AppUser.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
    }

    public static UserDto toUserDto(AppUser user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .socialProfile(user.getSocialProfile())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserEvent toUserEvent(AppUser user, UserEventType type) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
