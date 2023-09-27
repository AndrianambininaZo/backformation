package com.streaming.arosaina.repository;

import com.streaming.arosaina.entity.Correction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrectionRepository extends JpaRepository<Correction,Long> {
}
