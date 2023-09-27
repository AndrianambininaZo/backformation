package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.CorrectionRequest;
import com.streaming.arosaina.entity.Correction;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.repository.CorrectionRepository;
import com.streaming.arosaina.service.CorrectionService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class CorrectionController {
    private CorrectionService correctionService;
    private CorrectionRepository repository;
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation/correction";

    @PostMapping("/correction_save")
    public ResponseEntity<Correction> saveCorrection(@RequestBody CorrectionRequest request){
        return ResponseEntity.ok(correctionService.saveCorrection(request));
    }

    @PostMapping("/upload_correction/{id}")
    public ResponseEntity<String> saveFileCorrection(@RequestParam("files") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(correctionService.saveFileCorrection(file,id));
    }
    @GetMapping("/list_corrections/{idModule}")
    public ResponseEntity<List<Correction>> listCorrection(@PathVariable("idModule") Long idModule){
        return ResponseEntity.ok(correctionService.listCorrection(idModule));
    }
    @GetMapping("/list_corrections_by_client/{idClient}")
    public ResponseEntity<List<Correction>> listCorrectionByClient(@PathVariable("idClient") String idClient){
        return ResponseEntity.ok(correctionService.listCorrectionByUser(idClient));
    }
    @GetMapping("/telecharger_correction/{id}")
    public ResponseEntity<Resource> telechargerDoc(@PathVariable("id") Long idFileByModule) throws IOException {
        Correction modFile=repository.findById(idFileByModule).get();
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

}
