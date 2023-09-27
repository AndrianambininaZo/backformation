package com.streaming.arosaina.service.impl;


import com.streaming.arosaina.dto.ExamenRequest;
import com.streaming.arosaina.dto.ListExamen;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.repository.ExamenRepository;
import com.streaming.arosaina.repository.FileByModuleRepository;
import com.streaming.arosaina.repository.ModuleRepository;
import com.streaming.arosaina.service.ExamenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;
    private ModuleRepository repository;
    private ApplicationUserRepository userRepository;
    private FileByModuleRepository moduleRepository;
    @Override
    public Examen saveExamen(ExamenRequest request) {
        /*
        Examen examen=new Examen();
        examen.setDate(request.getDate());
        examen.setStatus(request.getStatus());
        System.out.println(request);
        User user=userRepository.findById(request.getIdUser()).get();
        FileByModule fileByModule=moduleRepository.findById(request.getIdFile()).get();
        examen.setUser(user);
        examen.setFileByModule(fileByModule);*/
        Examen examen=examenRepository.findById(request.getId()).get();
        examen.setStatus(1);
        examen.setIsCorrection(0);
        return examenRepository.save(examen);
    }

    @Override
    public void addUserByExamen(ApplicationUser user, Long idModule) {
        ModuleFormation module=repository.findById(idModule).get();
        List<FileByModule> list=moduleRepository.findByModuleAndType(module,"document");
        System.out.println(list);

        if(list.size()!=0){
            for (FileByModule fileByModule:list){
                Examen examen=new Examen();
                examen.setUser(user);
                examen.setStatus(0);
                examen.setIsCorrection(0);
                examen.setFileByModule(fileByModule);
                examen.setDate(new Date());
                examen.setIdModule(fileByModule.getModule().getId());
                examenRepository.save(examen);
            }
        }

    }

    @Override
    public List<Examen> listExamen() {
        return examenRepository.findAll();
    }

    @Override
    public List<ListExamen> listExamenBySum(Long idModule) {
        List<Object[]> results=examenRepository.listBExamenBySum(idModule);
        List<ListExamen> listExamens=new ArrayList<>();
        for (Object[] resultat:results){
            ApplicationUser user= (ApplicationUser) resultat[0];
            Long id=(Long) resultat[1];
            Long sumStatus=(Long) resultat[2];
            ListExamen listExamen=new ListExamen();
            listExamen.setIdModule(id);
            listExamen.setUser(user);
            listExamen.setSumStatus(Math.toIntExact(sumStatus));
            listExamens.add(listExamen);
        }
        List<ListExamen> examensList =new ArrayList<>();
        for (ListExamen examen:listExamens){
            if (examen.getUser().getStatus()==1){
                examensList.add(examen);
            }
        }
        return examensList;
    }

    @Override
    public List<Examen> listExamenFindBy(String idClient) {
        ApplicationUser user=userRepository.findById(idClient).get();
        return examenRepository.findByUser(user);
    }
    @Override
    public List<Examen> listExamenFindByClient(String idClient,Long idModule) {
        ApplicationUser user=userRepository.findById(idClient).get();
        return examenRepository.findByUserAndIdModule(user,idModule);
    }

    @Override
    public Examen annuller(Long idExamen) {
        Examen examen=examenRepository.findById(idExamen).get();
        examen.setStatus(0);
        return examen;
    }

    @Override
    public Integer sizeExamenByModule(Long idModule) {
        ModuleFormation module=repository.findById(idModule).get();
        List<FileByModule> list=moduleRepository.findByModuleAndType(module,"document");
        return list.size();
    }
}