package com.streaming.arosaina.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String id;
    private String nom;
    private String prenom;
    private String adresse;
}
