package com.streaming.arosaina.web;

import com.streaming.arosaina.dto.FileByModuleRequest;
import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.service.FileByModuleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FileByModuleController {
    private FileByModuleService fileByModuleService;
    @PostMapping("/save_file_by_module")
    public FileByModule saveFileByModule(@RequestBody FileByModuleRequest request){
        return fileByModuleService.saveFileByModule(request);
    }
    @GetMapping("/file_by_module/{idModule}")
    public List<FileByModule> listFileByModule(@PathVariable("idModule")Long idModule){
        return fileByModuleService.listFileByModule(idModule);
    }
    @GetMapping("/file_by_idModule/{idModule}")
    public FileByModule fileByModule(@PathVariable("idModule")Long idModule){
        return fileByModuleService.fileByModule(idModule);
    }
}
