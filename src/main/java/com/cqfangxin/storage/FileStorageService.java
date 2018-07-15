package com.cqfangxin.storage;

import com.cqfangxin.exception.StorageException;
import com.cqfangxin.web.CategoryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${web.upload.dir}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file, String filePath) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            File destDir = new File(uploadDir + filePath);
            if(!destDir.exists()){
                destDir.mkdirs();
            }
            Path path = Paths.get(destDir + "/" + filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public int deleteFile(String filePath){
        try{
            Path path = Paths.get(uploadDir + filePath);
            boolean isSuccess = Files.deleteIfExists(path);
            if(isSuccess){
                logger.info("Successfully delete file in "+ filePath);
                return 0;
            }else{
                logger.info("Failed delete file in "+ filePath);
                return -1;
            }
        }catch (IOException e) {
            throw new StorageException("Failed to delete file in " + filePath, e);
        }
    }


}
