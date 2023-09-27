package com.streaming.arosaina.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser  {
    @Id
    private String id;
    private String nom;
    private Integer status;
    private Integer etat;
    private String prenom;
    private String adresse;
    private String telephone;
    private String fileName;
    @Lob
    private byte[] data;

    private String ext;
    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role;
    private Integer payer;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Examen> examens;
}
