package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.enumaration.VerificationUrlType;
import io.dmly.invoicer.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerificationUrl(User user, String url, VerificationUrlType type) {
        if (log.isInfoEnabled()) {
            log.info(">>>> Sending verification URL {} on email {}", url, user.getEmail());
        }
    }
}
