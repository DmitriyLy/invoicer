package io.dmly.invoicer.service;

import io.dmly.invoicer.model.Role;

import java.util.Collection;

public interface RoleService {
    Role getRoleByUserId(Long userId);
    Collection<Role> getRoles();
    void updateUserRole(Long userId, String roleName);
}
