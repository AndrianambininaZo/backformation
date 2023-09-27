package com.streaming.arosaina.service;

import com.streaming.arosaina.dto.LoginResponse;
import com.streaming.arosaina.dto.UpdatePasswordRequest;
import com.streaming.arosaina.dto.UserRequest;
import com.streaming.arosaina.dto.UserUpdateRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ApplicationUserService {
    ApplicationUser saveUser(UserRequest request);

    void initialisationSuperAdmin();

    ApplicationUser saveUserAdmin(UserRequest request);

    String uploadImage(MultipartFile file, String idUser);

    public String saveFileImage(MultipartFile file, String id) throws IOException;

    byte[] telechargerImage(String idUser);

    LoginResponse login(String email, String password);
    List<ApplicationUser> listUser();
    ApplicationUser user(String id);
    ApplicationUser modifierStatus(String id);
    ApplicationUser userByEmail(String email);
    ApplicationUser updatePassword(UpdatePasswordRequest request);
    ApplicationUser modifierInfoUser(UserUpdateRequest request);
    String deleteUser(String  idUser);
    ApplicationUser isPayement(String  idUser);
}
