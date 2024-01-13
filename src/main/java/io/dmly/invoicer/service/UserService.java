package io.dmly.invoicer.service;

import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.ChangePasswordForm;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
    void sendVerificationCode(User userDto);
    User getUserByEmailAndValidCode(String email, String code);
    void resetPassword(String email);
    User verifyPasswordReset(String key);
    void changePassword(String key, ChangePasswordForm changePasswordData);
}
