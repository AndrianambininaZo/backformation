package com.streaming.arosaina.web;

import com.streaming.arosaina.repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api_test")
@CrossOrigin("*")
@AllArgsConstructor
public class Test {
    private ApplicationUserRepository userRepository;
    @GetMapping
    public String test(){
        return "test is fonction";
    }

}
