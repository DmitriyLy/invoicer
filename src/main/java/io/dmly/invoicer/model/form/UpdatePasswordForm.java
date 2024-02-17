package io.dmly.invoicer.model.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {
    @NotEmpty(message = "Current password is not specified")
    private String currentPassword;

    @NotEmpty(message = "New password is not specified")
    private String newPassword;

    @NotEmpty(message = "Confirm password is not specified")
    private String confirmPassword;
}
