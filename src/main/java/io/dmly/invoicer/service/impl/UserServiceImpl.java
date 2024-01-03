package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;

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

    protected void sendCodeViaSms(User userDto, String verificationCode, Date codeExpirationDate) {
        String message = String.format("Invoicer verification code: %s, active till %s", verificationCode, codeExpirationDate);
        log.info("-----> Sending verification code in SMS >>>>>>>> " + message);
    }
}
