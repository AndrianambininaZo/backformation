package com.streaming.arosaina.service;

import com.streaming.arosaina.dto.ExamenRequest;
import com.streaming.arosaina.dto.ListExamen;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.Examen;

import java.util.List;

public interface ExamenService {
    Examen saveExamen(ExamenRequest request);

    void addUserByExamen(ApplicationUser user, Long idModule);

    List<Examen> listExamen();

    List<ListExamen> listExamenBySum(Long idModule);

    List<Examen>listExamenFindBy(String idClient);

    List<Examen> listExamenFindByClient(String idClient,Long idModule);

    Examen annuller(Long idExamen);
    Integer sizeExamenByModule(Long idModule);

}
