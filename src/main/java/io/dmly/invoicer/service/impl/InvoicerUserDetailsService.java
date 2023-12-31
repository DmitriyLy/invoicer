package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.InvoicerUserDetails;
import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.repository.RoleRepository;
import io.dmly.invoicer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoicerUserDetailsService implements UserDetailsService {
    private final UserRepository<User> userUserRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public InvoicerUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userUserRepository.getUserByEmail(email);

        if (user.isEmpty()) {
            log.error("Cannot find a user by provided email: {}", email);
            throw new UsernameNotFoundException(String.format("Cannot find a user by provided email: %s", email));
        }

        log.info("User found: {}", user);

        Long userId = user.get().getId();

        Optional<Role> role = roleRepository.getRoleByUserId(userId);
        if (role.isEmpty()) {
            log.error("Cannot find role by user id {}", userId);
            throw new ApiException(String.format("Cannot find role by user id %s", userId));
        }

        return new InvoicerUserDetails(user.get(), role.get().getPermission());
    }
}
