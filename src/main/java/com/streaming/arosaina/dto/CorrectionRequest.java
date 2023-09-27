package com.streaming.arosaina.dto;

import com.streaming.arosaina.entity.Examen;
import lombok.Data;

@Data
public class CorrectionRequest {
    private Long id;
    private Long idExamen;
    private String status;
}
