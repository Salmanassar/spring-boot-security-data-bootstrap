package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import web.service.UserService;
import web.util.CurrentUser;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private CurrentUser currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView theUserInformation(Principal principal) {
        return new ModelAndView("user/userList", "user", currentUser.getCurrentUser(principal));
    }
}
