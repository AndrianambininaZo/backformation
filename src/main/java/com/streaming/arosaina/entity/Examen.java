package com.streaming.arosaina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Examen {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    private FileByModule fileByModule;
    @ManyToOne
    private ApplicationUser user;
    private Date date;
    private Integer status;
    private Integer isCorrection;
    private Long idModule;



}
