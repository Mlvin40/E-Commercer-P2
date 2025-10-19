package ecommerce_api.ecommerce_api.controller;

import ecommerce_api.ecommerce_api.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService storage;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    HttpServletRequest request) throws Exception {
        // 1) Guardar y obtener ruta relativa (p. ej. "/files/uuid.png")
        String relativeUrl = storage.saveImage(file);

        // 2) Construir URL absoluta según la solicitud actual
        String absoluteUrl = ServletUriComponentsBuilder
                .fromRequestUri(request)      // http://host:port/api/files/upload
                .replacePath(relativeUrl)     // -> http://host:port/files/uuid.png
                .build()
                .toUriString();

        // 201 Created con Location y el cuerpo con la URL absoluta
        return ResponseEntity
                .created(URI.create(absoluteUrl))
                .body(Map.of("url", absoluteUrl));
    }
}
