package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.entitymapper.UserDtoMapper;
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
    public void sendVerificationCode(UserDto userDto) {
        String verificationCode = RandomStringUtils.randomAlphabetic(8);
        Date codeExpirationDate = DateUtils.addDays(new Date(), 1);
        userRepository.updateVerificationCodeForUser(userDto, verificationCode, codeExpirationDate);
        sendCodeViaSms(userDto, verificationCode, codeExpirationDate);
    }

    protected void sendCodeViaSms(UserDto userDto, String verificationCode, Date codeExpirationDate) {
        String message = String.format("Invoicer verification code: %s, active till %s", verificationCode, codeExpirationDate);
        log.info("-----> Sending verification code in SMS >>>>>>>> " + message);
    }
}
