package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDto createUser(User user) {
        return userDtoMapper.fromUser(userRepository.create(user));
    }
}
