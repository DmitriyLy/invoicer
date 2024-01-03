package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.repository.RoleRepository;
import io.dmly.invoicer.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRepository;

    @Override
    public Role getRoleByUserId(Long userId) {
        Optional<Role> roleByUserId = roleRepository.getRoleByUserId(userId);

        if (roleByUserId.isEmpty()) {
            log.error("Cannot find role by provided user ID: {}", userId);
            throw new ApiException("Cannot find user role");
        }

        return roleByUserId.get();
    }
}
