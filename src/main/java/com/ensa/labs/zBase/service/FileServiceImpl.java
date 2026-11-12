package com.ensa.labs.zBase.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Images live in a single upload directory. Every path coming from a client is resolved
 * inside that directory, so files outside of it can never be read or deleted.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );
    private static final Pattern FOLDER_PATTERN = Pattern.compile("[a-z0-9-]{1,40}");

    private final Path uploadRoot;

    public FileServiceImpl(@Value("${app.files.upload-dir:uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public Resource getImageByPath(String path) throws MalformedURLException {
        Path imagePath = resolveInsideRoot(path);
        Resource resource = new UrlResource(imagePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        }
        throw new ResourceNotFoundException("Image", "path", path);
    }

    @Override
    public void deleteImageByPath(String path) {
        try {
            Files.deleteIfExists(resolveInsideRoot(path));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete image " + path, e);
        }
    }

    @Override
    public String storeImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The file is empty");
        }
        String extension = ALLOWED_TYPES.get(file.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("Only JPEG, PNG and WEBP images are accepted");
        }
        String safeFolder = folder == null || folder.isBlank() ? "misc" : folder.trim().toLowerCase();
        if (!FOLDER_PATTERN.matcher(safeFolder).matches()) {
            throw new IllegalArgumentException("Invalid folder name");
        }
        try {
            Path directory = uploadRoot.resolve(safeFolder);
            Files.createDirectories(directory);
            String fileName = UUID.randomUUID() + "." + extension;
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, directory.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return safeFolder + "/" + fileName;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store image", e);
        }
    }

    private Path resolveInsideRoot(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Missing image path");
        }
        Path resolved = uploadRoot.resolve(path.replace('\\', '/')).normalize();
        if (!resolved.startsWith(uploadRoot)) {
            throw new IllegalArgumentException("Invalid image path");
        }
        return resolved;
    }
}
