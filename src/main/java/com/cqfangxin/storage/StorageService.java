package com.cqfangxin.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String filePath);
    int deleteFile(String filePath);
}