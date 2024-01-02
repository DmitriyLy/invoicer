package io.dmly.invoicer.repository;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.model.User;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public interface UserRepository <T extends User> {

    T create(T user);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T user);
    Boolean delete(Long id);
    Optional<T> getUserByEmail(String email);
    void updateVerificationCodeForUser(UserDto userDto, String verificationCode, Date codeExpirationDate);
}
