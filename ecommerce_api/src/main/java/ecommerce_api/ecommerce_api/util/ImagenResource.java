package ecommerce_api.ecommerce_api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Base64;
import java.util.Locale;

/**
 * The type Imagen resource.
 */
@Component
public class ImagenResource {

    private static final Logger log = LoggerFactory.getLogger(ImagenResource.class);

    /**
     * Placeholder VISIBLE (SVG). Nota: %23 es '#' escapado.
     */
    public static final String PLACEHOLDER_VISIBLE =
            "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='48' height='48' viewBox='0 0 48 48'><rect width='48' height='48' fill='%23e5e7eb'/><path d='M10 36l9-12 6 8 6-7 7 11H10z' fill='%239ca3af'/><circle cx='18' cy='18' r='4' fill='%239ca3af'/></svg>";

    /**
     * PNG 1×1 transparente en Data URI (para fallbacks de solo-base64).
     */
    public static final String TRANSPARENT_PX_DATA_URI =
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=";

    /**
     * Solo la parte base64 del PNG 1×1.
     */
    public static final String TRANSPARENT_PX_BASE64 =
            TRANSPARENT_PX_DATA_URI.substring(TRANSPARENT_PX_DATA_URI.indexOf(',') + 1);

    private final Path root;

    /**
     * Instantiates a new Imagen resource.
     *
     * @param uploadDir the upload dir
     */
    public ImagenResource(@Value("${app.upload-dir}") String uploadDir) {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            log.error("No se pudo crear el directorio de uploads: {}", this.root, e);
        }
    }

    /**
     * Devuelve una Data URI lista para <img src="...">.
     * Tolerante: si no hay imagen o hay error, devuelve un placeholder VISIBLE.
     * Acepta: base64 ya embebido (data:*), URL absoluta o ruta relativa
     *
     * @param storedPathOrUrl the stored path or url
     * @return the string
     */
    public String formatoRealImagen(String storedPathOrUrl) {
        try {
            // Si ya viene como data URI, devolver tal cual
            if (storedPathOrUrl != null) {
                String trimmed = storedPathOrUrl.trim();
                if (!trimmed.isEmpty() && trimmed.startsWith("data:")) {
                    return trimmed;
                }
            }

            Path file = resolveAgainstRootOrNull(storedPathOrUrl);
            if (file == null) {
                return PLACEHOLDER_VISIBLE;
            }

            byte[] bytes = Files.readAllBytes(file);
            String mime  = detectMime(file);
            String b64   = Base64.getEncoder().encodeToString(bytes);
            return "data:" + mime + ";base64," + b64;

        } catch (Exception ex) {
            log.warn("Fallo generando data URI para [{}]: {}", storedPathOrUrl, ex.getMessage());
            return PLACEHOLDER_VISIBLE; // fallback visible
        }
    }

    /**
     * Devuelve solo el base64 (sin el prefijo data:...).
     * Tolerante: si no hay imagen, devuelve un 1×1 PNG transparente en base64.
     *
     * @param storedPathOrUrl the stored path or url
     * @return the string
     */
    public String toBase64Safe(String storedPathOrUrl) {
        try {
            if (storedPathOrUrl != null) {
                String s = storedPathOrUrl.trim();
                if (!s.isEmpty() && s.startsWith("data:")) {
                    int i = s.indexOf(',');
                    return (i >= 0) ? s.substring(i + 1) : s;
                }
            }

            Path file = resolveAgainstRootOrNull(storedPathOrUrl);
            if (file == null) return TRANSPARENT_PX_BASE64;

            byte[] bytes = Files.readAllBytes(file);
            return Base64.getEncoder().encodeToString(bytes);

        } catch (Exception ex) {
            log.warn("Fallo generando base64 para [{}]: {}", storedPathOrUrl, ex.getMessage());
            return TRANSPARENT_PX_BASE64;
        }
    }

    // ---------- helpers ----------

    /** Resuelve contra app.upload-dir. Si no es válido/encontrado, devuelve null en lugar de lanzar. */
    private Path resolveAgainstRootOrNull(String storedPathOrUrl) {
        if (storedPathOrUrl == null || storedPathOrUrl.isBlank()) return null;
        String p = storedPathOrUrl.trim();

        // Si viene URL absoluta, extrae el path
        if (p.startsWith("http://") || p.startsWith("https://")) {
            try {
                p = new URI(p).getPath();
            } catch (URISyntaxException e) {
                return null;
            }
        }

        // Normaliza a nombre dentro de /files
        p = p.replace('\\', '/');
        if (p.startsWith("/files/")) p = p.substring("/files/".length());
        while (p.startsWith("/")) p = p.substring(1);
        if (p.contains("..") || p.isBlank()) return null;

        Path file = root.resolve(p).normalize();
        if (!file.startsWith(root)) return null;
        if (!Files.exists(file)) return null;
        return file;
    }

    private String detectMime(Path file) {
        try {
            String probed = Files.probeContentType(file);
            if (probed != null) return probed;
        } catch (IOException ignored) {}
        String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".webp")) return "image/webp";
        if (name.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }
}
