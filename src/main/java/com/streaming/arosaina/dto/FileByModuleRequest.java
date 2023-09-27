package com.streaming.arosaina.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FileByModuleRequest {
    private Long id;
    private String fileName;
    private String type;
    private String extension;
    private Date date;
    private Long idModule;
}

