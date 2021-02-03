package com.vuetify.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.vuetify.utils.FileResponse;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);
    
    FileResponse uploadFile(MultipartFile file);

    String storeAndRename(MultipartFile file, Path newName);
    
    void deleteAll();

}
