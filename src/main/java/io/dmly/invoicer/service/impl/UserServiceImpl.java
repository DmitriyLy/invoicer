package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.ResetPasswordVerificationEntity;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.enumaration.VerificationUrlType;
import io.dmly.invoicer.model.form.ChangePasswordForm;
import io.dmly.invoicer.model.form.UpdatePasswordForm;
import io.dmly.invoicer.model.form.UpdateUserDetailsForm;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.service.EmailService;
import io.dmly.invoicer.service.UserService;
import io.dmly.invoicer.service.VerificationUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final VerificationUrlGenerator verificationUrlGenerator;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        return userRepository.create(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .getUserByEmail(email)
                .orElseThrow(() -> new ApiException("Cannot find user by email: " + email));
    }

    @Override
    public void sendVerificationCode(User user) {
        String verificationCode = RandomStringUtils.randomAlphabetic(8);
        Date codeExpirationDate = DateUtils.addDays(new Date(), 1);
        userRepository.updateVerificationCodeForUser(user, verificationCode, codeExpirationDate);
        sendCodeViaSms(user, verificationCode, codeExpirationDate);
    }

    @Override
    public User getUserByEmailAndValidCode(String email, String code) {
        Optional<User> user = userRepository.getUserByEmailAndValidCode(email, code);

        if (user.isEmpty()) {
            log.error("Cannot find a user by passed email {} and code {}", email, code);
            throw new ApiException("Cannot verify a user by passed parameters");
        }

        userRepository.deleteVerificationCodeByUserId(user.get().getId());
        return user.get();
    }

    @Override
    public void resetPassword(String email) {
        User user = getUserByEmail(email);
        String key = verificationUrlGenerator.getUniqueKey();
        String url = verificationUrlGenerator.generateVerificationUrl(key, VerificationUrlType.PASSWORD);
        Date expirationDate = DateUtils.addDays(new Date(), 1);
        userRepository.updateResetPasswordVerification(user, key, expirationDate);
        emailService.sendVerificationUrl(user, url, VerificationUrlType.PASSWORD);
    }

    @Override
    public User verifyPasswordReset(String key) {
        Optional<ResetPasswordVerificationEntity> optionalEntity = userRepository.getResetPasswordVerificationEntityByKey(key);
        if (optionalEntity.isEmpty()) {
            throw new ApiException("Cannot find record by specified key");
        }

        var resetPasswordVerificationEntity = optionalEntity.get();

        if (LocalDateTime.now().isAfter(resetPasswordVerificationEntity.expirationDate())) {
            throw new ApiException("Password reset request has expired. Please repeat operation.");
        }

        return userRepository
                .get(resetPasswordVerificationEntity.userId())
                .orElseThrow(this::getUserNotFoundException);
    }

    @Override
    public void changePassword(String key, ChangePasswordForm changePasswordData) {
        if (StringUtils.isEmpty(key)) {
            throw new ApiException("Key cannot be empty");
        }

        if (changePasswordData == null ||
                StringUtils.isEmpty(changePasswordData.password()) ||
                StringUtils.isEmpty(changePasswordData.password())) {
            throw new ApiException("Password data not specified.");
        }

        if (!StringUtils.equals(changePasswordData.password(), changePasswordData.confirmation())) {
            throw new ApiException("Password and confirmation do not match.");
        }

        userRepository.savePasswordByResetPasswordKey(key, changePasswordData.password());
    }

    @Override
    public void activateAccount(String email) {
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ApiException("Cannot find user by specified email: " + email);
        }

        User user = optionalUser.get();

        if (user.isEnabled()) {
            throw new ApiException("Account has been already activated");
        }

        userRepository.setUserAccountEnabled(user.getId());
    }

    @Override
    public User updateUserRetails(UpdateUserDetailsForm updateDetails) {
        userRepository.updateDetails(updateDetails);
        return userRepository
                .get(updateDetails.getId())
                .orElseThrow(this::getUserNotFoundException);
    }

    @Override
    public void updatePassword(Long id, UpdatePasswordForm form) {
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new ApiException("Password and confirmation don't match, Please try again.");
        }

        var user = get(id);

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new ApiException("Current password is incorrect. Please try again.");
        }

        userRepository.updatePassword(id, form.getNewPassword());
    }

    @Override
    public User get(Long id) {
        return userRepository.get(id).orElseThrow(() -> new ApiException("Cannot find user by specified id"));
    }

    protected void sendCodeViaSms(User userDto, String verificationCode, Date codeExpirationDate) {
        String message = String.format("Invoicer verification code: %s, active till %s", verificationCode, codeExpirationDate);
        log.info("-----> Sending verification code in SMS >>>>>>>> " + message);
    }

    private ApiException getUserNotFoundException() {
        return new ApiException("Cannot find a user by id");
    }
}
