package io.dmly.invoicer.service;

import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.ChangePasswordForm;
import io.dmly.invoicer.model.form.UpdateAccountSettingsForm;
import io.dmly.invoicer.model.form.UpdatePasswordForm;
import io.dmly.invoicer.model.form.UpdateUserDetailsForm;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
    void sendVerificationCode(User userDto);
    User getUserByEmailAndValidCode(String email, String code);
    void resetPassword(String email);
    User verifyPasswordReset(String key);
    void changePassword(String key, ChangePasswordForm changePasswordData);
    void activateAccount(String email);
    User updateUserRetails(UpdateUserDetailsForm updateDetails);
    void updatePassword(Long id, UpdatePasswordForm form);
    User get(Long id);
    void updateAccountSettings(Long id, UpdateAccountSettingsForm form);
    void toggleMfa(Long id);
    void uploadImage(User user, MultipartFile image);
}
