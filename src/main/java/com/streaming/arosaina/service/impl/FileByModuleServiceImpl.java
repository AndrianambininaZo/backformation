package com.streaming.arosaina.service.impl;

import com.streaming.arosaina.dto.FileByModuleRequest;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.entity.Validation;
import com.streaming.arosaina.repository.ExamenRepository;
import com.streaming.arosaina.repository.FileByModuleRepository;
import com.streaming.arosaina.repository.ModuleRepository;
import com.streaming.arosaina.repository.ValidationRepository;
import com.streaming.arosaina.service.FileByModuleService;
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

import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@AllArgsConstructor
@Transactional
public class FileByModuleServiceImpl implements FileByModuleService {
    public static final String DIRECTORY_FILE_COR=System.getProperty("user.home") + "/Download/uploads/formation/correction";
    public static final String DIRECTORY_FILE_EXM=System.getProperty("user.home") + "/Download/uploads/formation/examen";
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation";


    private ModuleRepository moduleRepository;
    private FileByModuleRepository fileByModuleRepository;
    private ValidationRepository validationRepository;
    private ExamenRepository examenRepository;

    @Override
    public FileByModule saveFileByModule(FileByModuleRequest request) {
        FileByModule byModule=new FileByModule();
        byModule.setFileName(request.getFileName());
        byModule.setDate(request.getDate());
        byModule.setExtension(request.getExtension());
        byModule.setType(request.getType());
        ModuleFormation moduleFormation = moduleRepository.findById(request.getIdModule()).get();
        byModule.setModule(moduleFormation);
        FileByModule saveFileBy=fileByModuleRepository.save(byModule);

        List<Validation> validationList=validationRepository.findByModule(moduleFormation);
        if (validationList.size() !=0 && saveFileBy.getType().equals("document")){
            System.out.println(saveFileBy.getType());
            for (Validation validation:validationList){
                Examen examen=new Examen();
                examen.setUser(validation.getUser());
                examen.setStatus(0);
                examen.setFileByModule(saveFileBy);
                examen.setDate(new Date());
                examen.setIdModule(validation.getModule().getId());
                examenRepository.save(examen);
            }
        }
        return saveFileBy;
    }
    @Override
    public String saveFile(MultipartFile file, Long id) throws IOException {
        FileByModule fileByModule=fileByModuleRepository.findById(id).get();
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        String extension="";
        if(fileName.contains(".")){
            int i=fileName.lastIndexOf('.');
            extension=i>0 ? fileName.substring(i+1) :"";
            if (extension!=""){
                Path fileStorage= get(DIRECTORY_FILE,id+"."+extension).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                fileByModule.setExtension("."+extension);
                fileByModule.setFileName(fileName);
                fileByModuleRepository.save(fileByModule);
                return "felicitation";
            }else {
                return "il y une petite erreur";
            }
        }
        return fileName;
    }
    @Override
    public String saveFileExamen(MultipartFile file, Long id) throws IOException {
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        String extension="";
        if(fileName.contains(".")){
            int i=fileName.lastIndexOf('.');
            extension=i>0 ? fileName.substring(i+1) :"";
            if (extension!=""){
                Path fileStorage= get(DIRECTORY_FILE_EXM,id+"."+extension).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                return "felicitation";
            }else {
                return "il y une petite erreur";
            }
        }
        return fileName;
    }

    @Override
    public String saveFileCorrection(MultipartFile file, Long id) throws IOException {
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        String extension="";
        if(fileName.contains(".")){
            int i=fileName.lastIndexOf('.');
            extension=i>0 ? fileName.substring(i+1) :"";
            if (extension!=""){
                Path fileStorage= get(DIRECTORY_FILE_COR,id+"."+extension).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                return "felicitation";
            }else {
                return "il y une petite erreur";
            }
        }
        return fileName;
    }
    @Override
    public List<FileByModule> listFileByModule(Long idModule) {
        ModuleFormation formation=moduleRepository.findById(idModule).get();
        return fileByModuleRepository.findByModule(formation);
    }

    @Override
    public FileByModule fileByModule(Long idModule) {
        return fileByModuleRepository.findById(idModule).get();
    }


}
