package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
