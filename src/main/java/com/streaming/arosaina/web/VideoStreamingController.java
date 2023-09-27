package com.streaming.arosaina.web;

import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.repository.FileByModuleRepository;
import com.streaming.arosaina.service.FileByModuleService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VideoStreamingController {
    private FileByModuleService service;
    private FileByModuleRepository repository;
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation";

    @GetMapping("/videos")
    public ResponseEntity<InputStreamResource> streamVideo() throws IOException {
        String videoPath = DIRECTORY_FILE + File.separator + "test.mp4";
        Path path = Paths.get(videoPath);
        String fileName="test.mp4";
        //Path pathFile=get(DIRECTORY_AUDIO).toAbsolutePath().normalize().resolve(fileName);

        if (Files.exists(path)) {
            MediaType mediaType = MediaTypeFactory.getMediaType("test.mp4").orElse(MediaType.APPLICATION_OCTET_STREAM);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/streaming_videos/{nomVideos}")
    public ResponseEntity<InputStreamResource> streamVideos(@PathVariable("nomVideos") String nomVideos) throws IOException {
        String videoPath = DIRECTORY_FILE + File.separator + nomVideos;
        Path path = Paths.get(videoPath);
        //Path pathFile=get(DIRECTORY_AUDIO).toAbsolutePath().normalize().resolve(fileName);

        if (Files.exists(path)) {
            MediaType mediaType = MediaTypeFactory.getMediaType(nomVideos).orElse(MediaType.APPLICATION_OCTET_STREAM);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //uploade file

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> saveFile(@RequestParam("files") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(service.saveFile(file,id));
    }

    @PostMapping("/upload_examen/{id}")
    public ResponseEntity<String> saveFileExament(@RequestParam("files") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(service.saveFileExamen(file,id));
    }


    @GetMapping("/telechargerDoc/{id}")
    public ResponseEntity<Resource> telechargerDoc(@PathVariable("id") Long idFileByModule) throws  IOException{
        FileByModule modFile=repository.findById(idFileByModule).get();
        String fileName=idFileByModule+modFile.getExtension();
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
