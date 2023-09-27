package com.streaming.arosaina.service.impl;

import com.streaming.arosaina.dto.LoginResponse;
import com.streaming.arosaina.dto.UpdatePasswordRequest;
import com.streaming.arosaina.dto.UserRequest;
import com.streaming.arosaina.dto.UserUpdateRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.service.ApplicationUserService;
import com.streaming.arosaina.service.ExamenService;
import com.streaming.arosaina.service.ModuleService;
import com.streaming.arosaina.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class UserServiceImpl implements ApplicationUserService {
    public static final String DIRECTORY_FILE=System.getProperty("user.home") + "/Download/uploads/formation/image";
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ExamenService examenService;
    @Autowired
    private ApplicationUserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;
    @Override
    public ApplicationUser saveUser(UserRequest request) {
        ApplicationUser user=new ApplicationUser();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail().trim());
        user.setNom(request.getNom().toUpperCase().trim());
        user.setPassword(getPasswordEncode(request.getPassword()));
        user.setStatus(1);
        user.setPayer(0);
        user.setAdresse(request.getAdresse());
        user.setEtat(0);
        user.setTelephone(request.getTelephone());
        user.setPrenom(request.getPrenom().trim());
        user.setRole("CLIENT");
        //user.setAuthorities(roleAdmin);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        ApplicationUser saveUser=  userRepository.save(user);
        moduleService.addModuleToUser(1,saveUser.getId());
        examenService.addUserByExamen(saveUser,1L);
        return saveUser;
    }
    @Override
    public ApplicationUser saveUserAdmin(UserRequest request) {
        ApplicationUser user=new ApplicationUser();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail().trim());
        user.setNom(request.getNom().toUpperCase().trim());
        user.setPassword(getPasswordEncode(request.getPassword()));
        user.setStatus(1);
        user.setPayer(1);
        user.setAdresse(request.getAdresse());
        user.setEtat(0);
        user.setTelephone(request.getTelephone());
        user.setPrenom(request.getPrenom().trim());
        user.setRole("ADMIN");
        ApplicationUser saveUser=userRepository.save(user);
        return saveUser;
    }

    @Override
    public String uploadImage(MultipartFile file, String idUser) {
        try {
            ApplicationUser image=userRepository.findById(idUser).get();
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getBytes());
            image.setFileName(file.getOriginalFilename());

            image.setData(file.getBytes());
            userRepository.save(image);
            return "Image uploaded successfully.";
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String saveFileImage(MultipartFile file, String id) throws IOException {
        ApplicationUser fileImage=userRepository.findById(id).get();
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        String extension="";
        if(fileName.contains(".")){
            int i=fileName.lastIndexOf('.');
            extension=i>0 ? fileName.substring(i+1) :"";
            if (extension!=""){
                Path fileStorage= get(DIRECTORY_FILE,id+"."+extension).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                fileImage.setExt("."+extension);
                fileImage.setFileName(fileName);
                userRepository.save(fileImage);
                return "felicitation";
            }else {
                return "il y une petite erreur";
            }
        }
        return fileName;
    }

    @Override
    public byte[] telechargerImage(String idUser) {
        Optional<ApplicationUser> imageOptional = userRepository.findById(idUser);
        if (imageOptional.isPresent()) {
            ApplicationUser image = imageOptional.get();
            return image.getData();
        }
        return null;
    }


    @Override
    public LoginResponse login(String email, String password) {

        try {
            Authentication auth=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password)
            );
            String token=tokenService.generateJwt(auth);
            String role=userRepository.findByEmail(email).getRole();
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setRole(role);
            loginResponse.setJwtToken(token);
            ApplicationUser user=userRepository.findByEmail(email);
            if (user.getPayer().equals(0)){
                return new LoginResponse();
            }
            return loginResponse;

        }catch (AuthenticationException e){
            System.out.println(e);
            return null;
        }

    }

    @Override
    public List<ApplicationUser> listUser() {
        return userRepository.findAll();
    }

    @Override
    public ApplicationUser user(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public ApplicationUser modifierStatus(String id) {
        ApplicationUser user=userRepository.findById(id).get();
        if (user.getStatus()==1){
            user.setStatus(0);
            return userRepository.save(user);
        }else {
            user.setStatus(1);
            return userRepository.save(user);

        }


    }

    @Override
    public ApplicationUser userByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ApplicationUser updatePassword(UpdatePasswordRequest request) {
        System.out.println(request);
        ApplicationUser user=userRepository.findById(request.getId()).get();
        String passwordEncode=user.getPassword();
        boolean passwordsMatch = passwordEncoder.matches(request.getPassword(), passwordEncode);
        System.out.println(passwordsMatch);
        if (passwordsMatch){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
           return userRepository.save(user);
        }else {
            return null;
        }

    }

    @Override
    public ApplicationUser modifierInfoUser(UserUpdateRequest request) {
        ApplicationUser user=userRepository.findById(request.getId()).get();
        if (!request.getNom().equals("")){
           user.setNom(request.getNom());
           return userRepository.save(user);
        }
        if (!request.getPrenom().equals("")){
            user.setPrenom(request.getPrenom());
            return userRepository.save(user);
        }
        if (!request.getAdresse().equals("")){
            user.setAdresse(request.getAdresse());
            return userRepository.save(user);
        }else {
            return null;
        }
    }

    @Override
    public String deleteUser(String idUser) {
        ApplicationUser user=userRepository.findById(idUser).get();
        userRepository.delete(user);
        return "ok";
    }

    @Override
    public ApplicationUser isPayement(String idUser) {
        ApplicationUser user=userRepository.findById(idUser).get();
        user.setPayer(1);
        return userRepository.save(user);
    }


    @Override
    public void initialisationSuperAdmin() {
        List<ApplicationUser> list=userRepository.findAll();
        if (list.size()==0){
            System.out.println("cout="+list.size());
            UserRequest user = new UserRequest();
            user.setPassword("arosaina12345");
            user.setNom("Arosaina");
            user.setEmail("arosaina@gmail.com");
            user.setPrenom("spider");
            user.setTelephone("0340000000");
            user.setAdresse("Anosipatrana");
            saveUserAdmin(user);

        }

    }

    public String getPasswordEncode(String password){
        return passwordEncoder.encode(password);
    }

}

