package io.dmly.invoicer.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveImage(MultipartFile image);
    byte[] getImage(String imageName);
}
