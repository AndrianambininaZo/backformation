package com.streaming.arosaina.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExamenRequest {
    private Long Id;
    private Long idFile;

    private String idUser;
    private Date date;
    private Integer status;
}