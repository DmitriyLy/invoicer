package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.ResetPasswordVerificationEntity;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.ChangePasswordForm;
import io.dmly.invoicer.model.form.UpdateAccountSettingsForm;
import io.dmly.invoicer.model.form.UpdatePasswordForm;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.service.EmailService;
import io.dmly.invoicer.service.ImageService;
import io.dmly.invoicer.service.VerificationUrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserRepository<User> userRepository;

    @Mock
    private VerificationUrlGenerator verificationUrlGenerator;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }

    @Test
    void createUser() {
        when(userRepository.create(any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        assertNotNull(createdUser);
        assertEquals(testUser, createdUser);
    }

    @Test
    void getUserByEmail() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getUserByEmailAndValidCode() {
        when(userRepository.getUserByEmailAndValidCode(anyString(), anyString())).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUserByEmailAndValidCode("test@example.com", "123456");

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void resetPassword() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(verificationUrlGenerator.getUniqueKey()).thenReturn("123456");
        when(verificationUrlGenerator.generateVerificationUrl(anyString(), any())).thenReturn("http://example.com/resetPassword");
        assertDoesNotThrow(() -> userService.resetPassword("test@example.com"));
    }

    @Test
    void verifyPasswordResetWithValidKey() {
        var entity = new ResetPasswordVerificationEntity(null, 1L, null, LocalDateTime.now().plusDays(1));

        when(userRepository.getResetPasswordVerificationEntityByKey(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.get(anyLong())).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.verifyPasswordReset("123456"));
    }

    @Test
    void verifyPasswordResetWithExpiredKey() {
        var entity = new ResetPasswordVerificationEntity(
                null,
                null,
                null,
                LocalDateTime.now().minusDays(1)
        );

        when(userRepository.getResetPasswordVerificationEntityByKey(anyString())).thenReturn(Optional.of(entity));

        assertThrows(ApiException.class, () -> userService.verifyPasswordReset("expiredKey"));
    }

    @Test
    void changePasswordWithMatchingPasswords() {
        ChangePasswordForm form = new ChangePasswordForm("newPassword", "newPassword");

        assertDoesNotThrow(() -> userService.changePassword("key", form));
    }

    @Test
    void changePasswordWithNonMatchingPasswords() {
        ChangePasswordForm form = new ChangePasswordForm("newPassword", "differentPassword");

        assertThrows(ApiException.class, () -> userService.changePassword("key", form));
    }

    @Test
    void activateAccount() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.activateAccount("test@example.com"));
    }

    @Test
    void updatePasswordWithNonMatchingPasswords() {
        UpdatePasswordForm form = new UpdatePasswordForm();
        form.setCurrentPassword("oldPassword");
        form.setNewPassword("newPassword");
        form.setConfirmPassword("differentPassword");

        assertThrows(ApiException.class, () -> userService.updatePassword(1L, form));
    }

    @Test
    void get() {
        when(userRepository.get(anyLong())).thenReturn(Optional.of(testUser));

        User foundUser = userService.get(1L);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void updateAccountSettings() {
        UpdateAccountSettingsForm form = new UpdateAccountSettingsForm(false, false);

        assertDoesNotThrow(() -> userService.updateAccountSettings(1L, form));
    }

    @Test
    void toggleMfa() {
        assertDoesNotThrow(() -> userService.toggleMfa(1L));
    }
}
