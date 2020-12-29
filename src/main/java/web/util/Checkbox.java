package web.util;

import web.model.User;

public interface Checkbox {
    void selectRoleFromCheckbox(User user, boolean isAdmin, boolean isUser);
}
