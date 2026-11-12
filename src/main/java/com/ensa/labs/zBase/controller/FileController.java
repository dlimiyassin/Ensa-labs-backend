package com.ensa.labs.zBase.controller;

import com.ensa.labs.zBase.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /** Public: serves an uploaded image by its path relative to the upload directory. */
    @GetMapping("/get-image")
    public ResponseEntity<Resource> getImage(@RequestParam String path) {
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

    /** Any authenticated user may upload an image (e.g. a member picture); returns {"path": "..."}. */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "folder", defaultValue = "misc") String folder) {
        return Map.of("path", fileService.storeImage(file, folder));
    }

    @DeleteMapping("/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LAB_DIRECTOR')")
    public void deleteImage(@RequestParam String path) {
        fileService.deleteImageByPath(path);
    }
}
