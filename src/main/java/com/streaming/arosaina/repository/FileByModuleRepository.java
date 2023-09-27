package com.streaming.arosaina.repository;

import com.streaming.arosaina.entity.FileByModule;
import com.streaming.arosaina.entity.ModuleFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileByModuleRepository extends JpaRepository<FileByModule,Long> {
    List<FileByModule> findByModuleAndType(ModuleFormation moduleFormation, String type);
    List<FileByModule> findByModule(ModuleFormation moduleFormation);
}
