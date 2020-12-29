package web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@Service
public class CurrentUserImpl implements CurrentUser {

    @Autowired
    UserService userService;

    @Override
    public User getCurrentUser(Principal principal) {
        return userService.findUserByEmail(principal.getName()).orElseGet(User::new);
    }
}
