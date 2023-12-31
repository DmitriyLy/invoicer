package io.dmly.invoicer.service;

import io.dmly.invoicer.model.User;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
}
