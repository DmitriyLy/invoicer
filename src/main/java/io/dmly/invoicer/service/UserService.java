package io.dmly.invoicer.service;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.model.User;

public interface UserService {
    UserDto createUser(User user);
}
