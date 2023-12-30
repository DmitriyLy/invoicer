package io.dmly.invoicer.service;

import io.dmly.invoicer.model.enumaration.VerificationUrlType;

public interface VerificationUrlGenerator {
    String generateVerificationUrl(String key, VerificationUrlType type);

    String generateVerificationUrl(VerificationUrlType type);
}
