package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.UpdatePasswordRequest;
import com.streaming.arosaina.dto.UserRequest;
import com.streaming.arosaina.dto.UserUpdateRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.service.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class UtilisateurController {
    private ApplicationUserService service;
    private ApplicationUserRepository userRepository;
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation/image";

    @GetMapping("/get_utilisateurs")
    public List<ApplicationUser> list(){
        return service.listUser();
    }
    @GetMapping("/get_utilisateur/{id}")
    public ApplicationUser user(@PathVariable("id") String id){
        return service.user(id);
    }
    @GetMapping("/utilisateur_email/{email}")
    public ApplicationUser userByEmail(@PathVariable("email") String email){
        return service.userByEmail(email);
    }
    @PostMapping("/utilisateur")
    public ApplicationUser enregistreUser(@RequestBody UserRequest user){

        return service.saveUser(user);
    }
    @PostMapping("/utilisateurAdmi")
    public ApplicationUser enregistreUserAdmin(@RequestBody UserRequest user){

        return service.saveUserAdmin(user);
    }
    @GetMapping("/modifier_status_user/{idClient}")
    public ResponseEntity<ApplicationUser> enregistreUser(@PathVariable("idClient") String id){
        return ResponseEntity.ok().body(service.modifierStatus(id));
    }

    /* upload image et tele*/
    @PostMapping("/upload_mon_image/{id}")
    public ResponseEntity<String> saveFileCorrection(@RequestParam("files") MultipartFile file, @PathVariable("id") String id) throws IOException {
        return ResponseEntity.ok(service.saveFileImage(file,id));
    }
    @PostMapping("/upload_image/{id}")
    public ResponseEntity<String> saveFile(@RequestParam("files") MultipartFile file, @PathVariable("id") String id) throws IOException {
        return ResponseEntity.ok(service.uploadImage(file,id));
    }
    @GetMapping("/telecharger_image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        Optional<ApplicationUser> imageOptional = userRepository.findById(id);
        if (imageOptional.isPresent()) {
            ApplicationUser image = imageOptional.get();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getData());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/tel_image/{id}")
    public ResponseEntity<byte[]> getSary(@PathVariable("id") String id){
        byte[] image=service.telechargerImage(id);
        if (image==null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        }
    }

    @GetMapping("/telechargerImage/{id}")
    public ResponseEntity<Resource> telechargerDoc(@PathVariable("id") String id) throws  IOException{
        ApplicationUser user=userRepository.findById(id).get();
        String fileName=id+user.getExt();
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
    @PostMapping("/update_password")
    public ResponseEntity<ApplicationUser> updatePassword(@RequestBody UpdatePasswordRequest request){
        ApplicationUser userService=service.updatePassword(request);
        if (userService!=null){
            return ResponseEntity.ok(userService);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/update_info_user")
    public ResponseEntity<ApplicationUser> updateInfoUser(@RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(service.modifierInfoUser(request));
    }
    @GetMapping("/delete_user/{id}")
    public ResponseEntity<String>deleteUser(@PathVariable("id") String idClient){
        if (service.deleteUser(idClient).equals("")){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(service.deleteUser(idClient));
        }
    }
    @GetMapping("/is_payement_user/{id}")
    public ResponseEntity<ApplicationUser>isPayement(@PathVariable("id") String idClient){
        try {
            ApplicationUser user = service.isPayement(idClient);
            return ResponseEntity.ok(user);
        }  catch (Exception e) {
            // Gérer d'autres exceptions génériques
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
