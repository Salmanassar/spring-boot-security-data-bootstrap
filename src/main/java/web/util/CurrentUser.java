package web.util;

import web.model.User;

import java.security.Principal;

public interface CurrentUser {
    User getCurrentUser(Principal principal);
}
