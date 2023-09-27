package com.streaming.arosaina.dto;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String nom;
    private Integer status;
    private String email;
    private String password;
    private String telephone;
    private String prenom;
    private String adresse;

}

