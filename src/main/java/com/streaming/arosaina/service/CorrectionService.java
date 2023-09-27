package com.streaming.arosaina.service;

import com.streaming.arosaina.dto.CorrectionRequest;
import com.streaming.arosaina.entity.Correction;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CorrectionService {
    Correction saveCorrection(CorrectionRequest request);
    String saveFileCorrection(MultipartFile file, Long id) throws IOException;
    List<Correction> listCorrection(Long idModule);
    List<Correction> listCorrectionByUser(String idClient);
}
