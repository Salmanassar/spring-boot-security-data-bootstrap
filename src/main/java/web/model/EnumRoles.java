package web.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum EnumRoles {
    ROLE_USER(Set.of(Permission.USER_READ)),
    ROLE_ADMIN(Set.of(Permission.USER_READ, Permission.USER_WRITE));
    private final Set<Permission> permissions;

    EnumRoles(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
