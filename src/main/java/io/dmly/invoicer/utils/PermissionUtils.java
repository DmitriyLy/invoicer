package io.dmly.invoicer.utils;

import io.dmly.invoicer.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public final class PermissionUtils {
    private PermissionUtils() {}

    public static Collection<? extends GrantedAuthority> getAuthorities(String permissions) {
        return Arrays.stream(permissions.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return getAuthorities(role.getPermission());
    }
}
