package com.streaming.arosaina.dto;

import com.streaming.arosaina.entity.ApplicationUser;
import lombok.Data;

@Data
public class ListExamen {
    private ApplicationUser user;
    private Long idModule;
    private Integer sumStatus;
}
