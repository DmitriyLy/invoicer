package io.dmly.invoicer.service;

import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.enumaration.VerificationUrlType;

public interface EmailService {
    void sendVerificationUrl(User user, String url, VerificationUrlType type);
}
