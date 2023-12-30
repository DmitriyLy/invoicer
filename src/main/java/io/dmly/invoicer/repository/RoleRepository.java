package io.dmly.invoicer.repository;

import io.dmly.invoicer.model.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {

    T create(T role);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T role);
    Boolean delete(Long id);
    void addRoleToUser(Long userId, String roleName);
    T getRoleByUserId(Long userId);
    T getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
