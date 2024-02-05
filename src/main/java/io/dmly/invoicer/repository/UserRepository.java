package io.dmly.invoicer.repository;

import io.dmly.invoicer.model.ResetPasswordVerificationEntity;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.UpdateUserDetailsForm;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public interface UserRepository <T extends User> {

    T create(T user);
    Collection<T> list(int page, int pageSize);
    Optional<T>  get(Long id);
    T update(T user);
    Boolean delete(Long id);
    Optional<T> getUserByEmail(String email);
    void updateVerificationCodeForUser(User user, String verificationCode, Date codeExpirationDate);
    Optional<T> getUserByEmailAndValidCode(String email, String code);
    void deleteVerificationCodeByUserId(Long userId);
    void updateResetPasswordVerification(User user, String key, Date codeExpirationDate);
    Optional<ResetPasswordVerificationEntity> getResetPasswordVerificationEntityByKey(String key);
    void savePasswordByResetPasswordKey(String key, String password);
    void setUserAccountEnabled(Long id);
    void updateDetails(UpdateUserDetailsForm updateDetails);
}
