package com.streaming.arosaina.service.impl;

import com.streaming.arosaina.dto.CorrectionRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.Correction;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.repository.CorrectionRepository;
import com.streaming.arosaina.repository.ExamenRepository;
import com.streaming.arosaina.service.CorrectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
@AllArgsConstructor
public class CorrectionServiceImpl implements CorrectionService {
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation/correction";
    private CorrectionRepository repository;
    private ExamenRepository examenRepository;
    private ApplicationUserRepository userRepository;
    @Override
    public Correction saveCorrection(CorrectionRequest request) {
        Correction correction=new Correction();
        correction.setDate(new Date());
        correction.setStatus(request.getStatus());
        Examen examen=examenRepository.findById(request.getIdExamen()).get();
        correction.setExamen(examen);
        return repository.save(correction);
    }

    @Override
    public String saveFileCorrection(MultipartFile file, Long id) throws IOException {
        Correction fileCorrection=repository.findById(id).get();
        Examen examen=fileCorrection.getExamen();

        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        String extension="";
        if(fileName.contains(".")){
            int i=fileName.lastIndexOf('.');
            extension=i>0 ? fileName.substring(i+1) :"";
            if (extension!=""){
                Path fileStorage= get(DIRECTORY_FILE,id+"."+extension).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                fileCorrection.setExtension("."+extension);
                fileCorrection.setFileName(fileName);
                examen.setStatus(1);
                examen.setIsCorrection(1);
                examenRepository.save(examen);
                repository.save(fileCorrection);
                return "felicitation";
            }else {
                return "il y une petite erreur";
            }
        }
        return fileName;
    }

    @Override
    public List<Correction> listCorrection(Long idModule) {
        List<Correction> correctionList=repository.findAll();
        List<Correction> listByModule=correctionList.stream().filter(res->res.getExamen().getIdModule()==idModule).collect(Collectors.toList());
        return listByModule;
    }

    @Override
    public List<Correction> listCorrectionByUser(String idClient) {
        ApplicationUser user=userRepository.findById(idClient).get();
        List<Correction> correctionList=repository.findAll();
        List<Correction> listByClient=correctionList.stream().filter(res->res.getExamen().getUser()==user).collect(Collectors.toList());
        return listByClient;
    }

}
