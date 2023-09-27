package com.streaming.arosaina.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String id;
    private String password;
    private String newPassword;
}
