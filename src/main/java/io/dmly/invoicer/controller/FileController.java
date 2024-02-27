package io.dmly.invoicer.controller;

import io.dmly.invoicer.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static io.dmly.invoicer.constants.Constants.USER_IMAGE_PATH;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final ImageService imageService;

    @GetMapping(USER_IMAGE_PATH + "/{imageName}")
    public byte[] getUserImage(@PathVariable("imageName") String imageName) {
        return imageService.getImage(imageName);
    }

}
