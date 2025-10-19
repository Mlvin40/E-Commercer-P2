package ecommerce_api.ecommerce_api.controller;

import ecommerce_api.ecommerce_api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService storage;

    @PostMapping("/upload")
    // @PreAuthorize("hasAnyRole('COMUN','MODERADOR','LOGISTICA','ADMIN')")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String url = storage.saveImage(file);
        // 201 Created con Location (opcional)
        return ResponseEntity.created(java.net.URI.create(url))
                .body(Map.of("url", url));
    }
}