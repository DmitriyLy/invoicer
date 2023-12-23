package io.dmly.invoicer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class User {
    private Long id;

    @NotEmpty(message = "First name cannot be empty.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please specify a valid email.")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;

    private String phone;
    private String address;
    private String title;
    private String bio;
    private boolean isEnabled;
    private boolean isNonLocked;
    private boolean isUsingMfa;
    private String imageUrl;
    private LocalDateTime createdAt;
}
