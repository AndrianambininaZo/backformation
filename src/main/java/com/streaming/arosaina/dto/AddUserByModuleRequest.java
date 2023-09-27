package com.streaming.arosaina.dto;

import lombok.Data;

@Data
public class AddUserByModuleRequest {
    private String idClient;
    private Long idModule;
}
