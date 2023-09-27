package com.streaming.arosaina.service.impl;

import com.streaming.arosaina.dto.ModuleRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.entity.Validation;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.repository.ModuleRepository;
import com.streaming.arosaina.repository.ValidationRepository;
import com.streaming.arosaina.service.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

@Transactional
@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private ModuleRepository moduleRepository;
    private ApplicationUserRepository userRepository;
    private ValidationRepository validationRepository;
    @Override
    public ModuleFormation saveModule(ModuleRequest request) {
        Long code =moduleRepository.findByMaxCode();
        if (code==null){
            code = 0L;
        }
        ModuleFormation formation=new ModuleFormation();
        formation.setDate(request.getDate());
        formation.setNom(request.getNom());
        formation.setCode(Math.toIntExact(code) + 1);
        return moduleRepository.save(formation);
    }
    @Override
    public void addModuleToUser(Integer code, String idUser) {
        ModuleFormation formation=moduleRepository.findByCode(code);
        ApplicationUser user=userRepository.findById(idUser).get();
        Validation validation=new Validation();
        validation.setUser(user);
        validation.setModule(formation);
        validationRepository.save(validation);
    }

    @Override
    public List<ModuleFormation> listModule() {
        return moduleRepository.findAll();
    }

    @Override
    public Long findMaxByCode() {
        Long code=moduleRepository.findByMaxCode();
        if(code==null){
            return (long) (0 + 1);
        }else {
            return moduleRepository.findByMaxCode() +  1;
        }
    }
    @Override
    public List<ModuleFormation> listModuleFindByClient(String idClient) {
        ApplicationUser user=userRepository.findById(idClient).get();
        List<Validation>validationList=validationRepository.findByUser(user);
        List<ModuleFormation> formationList=new ArrayList<>();
        for (Validation validation:validationList){
            ModuleFormation formation=validation.getModule();
            formationList.add(formation);
        }
        return formationList;
    }

    @Override
    public boolean moduleSuivantByClient(String idClient, Long idModule) {
        ApplicationUser user=userRepository.findById(idClient).get();
        List<Validation>validationList=validationRepository.findByUser(user);
        List<ModuleFormation> formationList=new ArrayList<>();
        for (Validation validation:validationList){
            ModuleFormation formation=validation.getModule();
            formationList.add(formation);
        }
        Long maxId=moduleRepository.findByMaxCode();
        if (maxId<idModule){
            return false;
        }
        boolean isPresent = true;
        for (ModuleFormation formation:formationList){
            if(formation.getId().equals(idModule)){
                isPresent = false;
                break;
            }
        }
        return isPresent;
    }
}
