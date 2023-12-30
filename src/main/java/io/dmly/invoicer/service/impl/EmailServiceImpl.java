package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.enumaration.VerificationUrlType;
import io.dmly.invoicer.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerificationUrl(User user, String url, VerificationUrlType type) {

    }
}
