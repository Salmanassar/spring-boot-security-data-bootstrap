package web.model;

public enum Permission {
    USER_READ("users:read"),
    USER_WRITE("users:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
