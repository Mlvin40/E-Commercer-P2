package ecommerce_api.ecommerce_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * The type File storage service.
 */
@Service
public class FileStorageService {

    private final Path root;

    /**
     * Instantiates a new File storage service.
     *
     * @param uploadDir the upload dir
     * @throws IOException the io exception
     */
    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
    }

    /**
     * Save image string.
     *
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Archivo vacío.");
        // Validar content-type
        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ct.startsWith("image/")) throw new IllegalArgumentException("Solo imágenes.");

        // Extensión segura
        String original = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot != -1) ext = original.substring(dot).toLowerCase(Locale.ROOT);

        String filename = UUID.randomUUID() + ext;
        Path target = root.resolve(filename).normalize();
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // URL pública que servirás desde WebConfig (/files/**)
        return "/files/" + filename;
    }
}
