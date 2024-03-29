package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.enumaration.VerificationUrlType;
import io.dmly.invoicer.service.VerificationUrlGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Service
public class VerificationUrlGeneratorImpl implements VerificationUrlGenerator {
    @Override
    public String generateVerificationUrl(String key, VerificationUrlType type) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/user/verify/" + type.getType() + "/" + key)
                .toUriString();
    }

    @Override
    public String generateVerificationUrl(VerificationUrlType type) {
        return generateVerificationUrl(getUniqueKey(), type);
    }

    @Override
    public String getUniqueKey() {
        return UUID.randomUUID().toString();
    }
}
