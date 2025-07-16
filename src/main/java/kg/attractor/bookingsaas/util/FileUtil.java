package kg.attractor.bookingsaas.util;

import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {
    private static String DIRECTORY = "data/photos";

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String uploadFile(MultipartFile file) throws IOException {
        String fileName = getUniqueId() + "_" + file.getOriginalFilename();
        Path directoryPath = Paths.get(DIRECTORY);

        if (!Files.isDirectory(directoryPath))
            Files.createDirectories(directoryPath);

        Path filePath = Paths.get(DIRECTORY, fileName);

        if (!Files.exists(filePath))
            Files.createFile(filePath);

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static String uploadFile(String filePath, MultipartFile file) throws IOException {
        DIRECTORY = filePath;
        String path = uploadFile(file);

        DIRECTORY = "data/photos";
        return path;
    }

    private static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    @SneakyThrows
    private static ResponseEntity<?> getResponseEntityForFile(String filename, String directoryName, MediaType mediaType) {
        try {
            byte[] image = Files.readAllBytes(Paths.get(directoryName + "/" + filename));
            Resource resource = new ByteArrayResource(image);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(resource.contentLength())
                    .contentType(mediaType)
                    .body(resource);
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Image not found");
        }
    }

    @SneakyThrows
    public static void deleteFile(String filePath) {
        Assert.notNull(filePath, "filePath must not be null");
        if (!Files.exists(Paths.get(filePath))) return;
        Files.delete(Path.of(filePath));
    }

    public static MediaType defineFileType(String filePath) throws IOException {
        Assert.notNull(filePath, "filePath must not be null");
        String fileType = Files.probeContentType(Paths.get(filePath));
        return MediaType.parseMediaType(fileType);
    }
}