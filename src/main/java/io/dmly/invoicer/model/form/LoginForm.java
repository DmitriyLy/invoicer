package io.dmly.invoicer.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please specify a valid email.")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;
}
