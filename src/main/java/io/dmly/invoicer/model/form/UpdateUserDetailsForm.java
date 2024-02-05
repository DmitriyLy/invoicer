package io.dmly.invoicer.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDetailsForm {
    @NotNull(message = "Id cannot be empty")
    private Long id;

    @NotEmpty(message = "First name cannot be empty.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please specify a valid email.")
    private String email;

    @Pattern(regexp = "^[+]?\\d+$", message = "Specified phone number is invalid")
    private String phone;
    private String address;
    private String title;
    private String bio;
    private String imageUrl;
}
