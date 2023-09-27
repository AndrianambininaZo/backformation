package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.AddUserByModuleRequest;
import com.streaming.arosaina.dto.ExamenByClientRequest;
import com.streaming.arosaina.dto.ExamenRequest;
import com.streaming.arosaina.dto.ListExamen;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.repository.ExamenRepository;
import com.streaming.arosaina.service.ExamenService;
import com.streaming.arosaina.service.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ExamenController {
    private ExamenService examenService;
    private ExamenRepository repository;
    private ApplicationUserRepository userRepository;
    private ModuleService moduleService;

    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation/examen";
    @PostMapping("/save_examen")
    public Examen saveExamen(@RequestBody ExamenRequest request){
        return examenService.saveExamen(request);
    }
    @GetMapping("/list_examen")
    public List<Examen> listExamen(){
        return examenService.listExamen();
    }

    @GetMapping("/list_examen_sum/{idModule}")
    public List<ListExamen> listExamenBySum(@PathVariable("idModule") Long idModule){

        return examenService.listExamenBySum(idModule);
    }

    @GetMapping("/list_examen_by_client/{idClient}")
    public List<Examen> listExamenByClient(@PathVariable("idClient") String idClient){
        return examenService.listExamenFindBy(idClient);
    }
    @PostMapping("/list_examen_by_client_and_module")
    public List<Examen> listExamenByClient(@RequestBody ExamenByClientRequest request){
        Long idModule=request.getIdModule();
        String idClient=request.getIdClient();
        return examenService.listExamenFindByClient(idClient,idModule);
    }

    @GetMapping("/telecharger_examen/{id}")
    public ResponseEntity<Resource> telechargerDoc(@PathVariable("id") Long idFileByModule) throws IOException {
        Examen modFile=repository.findById(idFileByModule).get();
        String fileName=idFileByModule+".docx";
        Path pathFile=get(DIRECTORY_FILE).toAbsolutePath().normalize().resolve(fileName);
        if (!Files.exists(pathFile)){
            throw new FileNotFoundException(fileName+" n' exist pas dans la base");
        }
        Resource resource=new UrlResource(pathFile.toUri());
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("File-Name",fileName);
        httpHeaders.add(CONTENT_DISPOSITION,"attachment;File-Name="+ resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(pathFile)))
                .headers(httpHeaders).body(resource);
    }

    @GetMapping("/examen_annuller/{id}")
    public ResponseEntity<Examen> annullerExamen(@PathVariable("id") Long idExamen){
        return ResponseEntity.ok(examenService.annuller(idExamen));
    }
    @GetMapping("/size_examen_by_module/{idModule}")
    public Integer size(@PathVariable("idModule") Long idModule){
        return examenService.sizeExamenByModule(idModule);
    }
    /*@PostMapping("/add_user_by_module")
    public ResponseEntity<String> addUserByModule(@RequestBody AddUserByModuleRequest request){
        ApplicationUser user=userRepository.findById(request.getIdClient()).get();
        try {
            Integer codeModule= Math.toIntExact(request.getIdModule());
            moduleService.addModuleToUser(codeModule,request.getIdClient());
            examenService.addUserByExamen(user,request.getIdModule());

            return ResponseEntity.ok("Ok");
        }catch (Exception e){
            throw new RuntimeException("il y a une erreur");
        }
    }*/
}
