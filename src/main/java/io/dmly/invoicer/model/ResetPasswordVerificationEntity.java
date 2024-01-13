package io.dmly.invoicer.model;

import java.time.LocalDateTime;

public record ResetPasswordVerificationEntity(Long id,
                                              Long userId,
                                              String key,
                                              LocalDateTime expirationDate) {
}
