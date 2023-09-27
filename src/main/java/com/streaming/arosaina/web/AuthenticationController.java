package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.LoginRequest;
import com.streaming.arosaina.dto.LoginResponse;
import com.streaming.arosaina.service.ApplicationUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api_auth")
public class AuthenticationController {
    @Autowired
    private ApplicationUserService service;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("salut api auth");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest request){
        LoginResponse response=service.login(request.getUsername(),request.getPassword());
        if (response==null) {
            return ResponseEntity.notFound().build();
        }
        if (response.getJwtToken().equals("")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(response);
    }
    @PostConstruct
    public void initialisationAdmin(){
        service.initialisationSuperAdmin();
    }
}
