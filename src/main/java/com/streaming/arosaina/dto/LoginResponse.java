package com.streaming.arosaina.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private String role;



}
