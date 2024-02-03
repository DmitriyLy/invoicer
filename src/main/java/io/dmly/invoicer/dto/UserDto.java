package io.dmly.invoicer.dto;

import java.time.LocalDateTime;

public record UserDto(Long id,
                      String firstName,
                      String lastName,
                      String email,
                      String phone,
                      String address,
                      String title,
                      String bio,
                      boolean isEnabled,
                      boolean isNonLocked,
                      boolean isUsingMfa,
                      String imageUrl,
                      LocalDateTime createdAt,
                      String roleName,
                      String permissions) {
}
