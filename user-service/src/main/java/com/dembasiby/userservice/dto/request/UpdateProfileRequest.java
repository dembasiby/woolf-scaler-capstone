package com.dembasiby.userservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String avatarUrl;
    private String socialProfile;
}
