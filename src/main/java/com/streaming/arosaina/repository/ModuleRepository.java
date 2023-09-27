package com.streaming.arosaina.repository;

import com.streaming.arosaina.entity.ModuleFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleRepository extends JpaRepository<ModuleFormation,Long> {
    ModuleFormation findByCode(Integer code);

    @Query("SELECT MAX(m.code) FROM ModuleFormation m " )
    Long findByMaxCode();
}

