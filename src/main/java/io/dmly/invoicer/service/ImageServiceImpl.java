package io.dmly.invoicer.service;

import io.dmly.invoicer.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${files.image.path.prefix:/invoicer/images}")
    private String pathPrefix;

    @Override
    public String saveImage(MultipartFile image) {

        if (image == null || image.isEmpty()) {
            log.error("The image is empty.");
            throw new ApiException("Cannot save empty image");
        }

        String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(image.getOriginalFilename());

        Path filePath = getImageDirPath().resolve(fileName);

        try {
            Files.copy(image.getInputStream(), filePath, REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Cannot save image {}", filePath);
            throw new ApiException("Cannot save image");
        }

        return fileName;
    }

    @Override
    public byte[] getImage(String imageName) {
        try {
            return Files.readAllBytes(getImageDirPath().resolve(imageName));
        } catch (IOException e) {
            log.error("Cannot get image", e);
            throw new ApiException("Cannot get image " + imageName);
        }
    }

    private Path getImageDirPath() {
        Path dir = Path.of(System.getProperty("user.home"), pathPrefix).toAbsolutePath().normalize();

        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                log.error("Cannot create dir {}", dir);
                throw new ApiException("Cannot create image dir");
            }
        }

        return dir;
    }
}
