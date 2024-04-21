package com.metehan.authenticationApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String lastname;
    private String email;
    private String password;

}
