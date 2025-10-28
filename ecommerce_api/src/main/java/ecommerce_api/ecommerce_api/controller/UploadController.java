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


//    @PostMapping("/upload")
//    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
//                                    HttpServletRequest request) throws Exception {
//        // Guardar y obtener ruta relativa
//        String relativeUrl = storage.saveImage(file);
//
//        // Construir URL absoluta según la solicitud actual
//        String absoluteUrl = ServletUriComponentsBuilder
//                .fromRequestUri(request)      // Esto toma el esquema, host, puerto y ruta actual
//                .replacePath(relativeUrl)     // Establece la nueva ruta
//                .build()
//                .toUriString();
//
//        // 201 Created con Location y el cuerpo con la URL absoluta
//        return ResponseEntity
//                .created(URI.create(absoluteUrl))
//                .body(Map.of("url", absoluteUrl));
//    }
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String relativeUrl = storage.saveImage(file); // "/files/uuid.ext"
        return ResponseEntity.ok(Map.of("url", relativeUrl));

    }
}
