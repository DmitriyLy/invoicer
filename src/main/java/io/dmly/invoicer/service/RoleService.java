package io.dmly.invoicer.service;

import io.dmly.invoicer.model.Role;

public interface RoleService {
    Role getRoleByUserId(Long userId);
}
