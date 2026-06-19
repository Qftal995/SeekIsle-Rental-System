package com.seekisle.fileservice.service;

import com.seekisle.fileservice.domain.dto.FileDTO;
import com.seekisle.fileservice.domain.dto.SignDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    FileDTO upload(MultipartFile file);

    SignDTO getSign();
}
