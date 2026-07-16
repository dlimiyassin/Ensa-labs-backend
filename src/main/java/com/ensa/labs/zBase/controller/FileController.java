package com.ensa.labs.zBase.controller;

import com.ensa.labs.zBase.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/get-image")
    public ResponseEntity<Resource>
    getImage(@RequestParam String path) {
        try {
            Resource image = fileService.getImageByPath(path);
            MediaType mediaType = MediaTypeFactory.getMediaType(image)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(image.contentLength())
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().noTransform())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                    .body(image);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestParam String path) {
        fileService.deleteImageByPath(path);
        return ResponseEntity.noContent().build();
    }
}
