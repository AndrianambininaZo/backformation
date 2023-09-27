package com.streaming.arosaina.service;

import com.streaming.arosaina.dto.ModuleRequest;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.entity.Validation;

import java.util.List;

public interface ModuleService {
    ModuleFormation saveModule(ModuleRequest request);
    void addModuleToUser(Integer code, String idUser);
    List<ModuleFormation> listModule();
    Long findMaxByCode();

    List<ModuleFormation> listModuleFindByClient(String idClient);


    boolean moduleSuivantByClient(String idClient, Long idModule);

}