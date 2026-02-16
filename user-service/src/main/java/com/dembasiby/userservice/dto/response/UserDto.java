package com.dembasiby.userservice.dto.response;

import com.dembasiby.userservice.model.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String avatarUrl;
    private String socialProfile;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
