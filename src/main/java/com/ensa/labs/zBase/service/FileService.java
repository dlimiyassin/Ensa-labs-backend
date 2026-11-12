package com.ensa.labs.zBase.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface FileService {
    /**
     * @param path path relative to the upload directory (as returned by {@link #storeImage})
     */
    Resource getImageByPath(String path) throws MalformedURLException;

    void deleteImageByPath(String path);

    /**
     * Stores a JPEG / PNG / WEBP image under {@code <upload-dir>/<folder>/} and returns its
     * path relative to the upload directory, e.g. {@code members/3f1c....png}.
     */
    String storeImage(MultipartFile file, String folder);
}
