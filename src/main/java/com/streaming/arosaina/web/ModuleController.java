package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.IsModuleSuivantRequest;
import com.streaming.arosaina.dto.ModuleRequest;
import com.streaming.arosaina.dto.ValidationRequest;
import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import com.streaming.arosaina.service.ExamenService;
import com.streaming.arosaina.service.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class ModuleController {
    private ModuleService moduleService;
    private ExamenService examenService;
    private ApplicationUserRepository userRepository;
    @PostMapping("/create_module")
    public ModuleFormation saveModule(@RequestBody ModuleRequest request){
        return moduleService.saveModule(request);
    }
    @PostMapping("/add_client_module")
    public void saveModule(@RequestBody ValidationRequest request){
        moduleService.addModuleToUser(request.getCode(),request.getIdUser());
        ApplicationUser user=userRepository.findById(request.getIdUser()).get();
        examenService.addUserByExamen(user, Long.valueOf(request.getCode()));

    }
    @GetMapping("/list_module")
    public List<ModuleFormation> listModule(){
        return  moduleService.listModule();
    }

    @GetMapping("/find_maxModule_by_code")
    public Long findByMax(){
        return  moduleService.findMaxByCode();
    }

    @GetMapping("/list_module_by_client/{idClient}")
    public List<ModuleFormation> listModuleByClient(@PathVariable("idClient") String idClient){
        return  moduleService.listModuleFindByClient(idClient);
    }
    @PostMapping("/is_Module_Suivant")
    public ResponseEntity<Boolean> IsModule(@RequestBody IsModuleSuivantRequest request){
        return ResponseEntity.ok(moduleService.moduleSuivantByClient(request.getIdClient(),request.getIdModule()));
    }
}
