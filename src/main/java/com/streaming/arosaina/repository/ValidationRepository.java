package com.streaming.arosaina.repository;

import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.ModuleFormation;
import com.streaming.arosaina.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValidationRepository extends JpaRepository<Validation,Long> {
    List<Validation> findByUser(ApplicationUser user);
    List<Validation> findByModule(ModuleFormation user);
}