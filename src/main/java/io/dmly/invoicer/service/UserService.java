package io.dmly.invoicer.service;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.model.User;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
    void sendVerificationCode(UserDto userDto);
}
