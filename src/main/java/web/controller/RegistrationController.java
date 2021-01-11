package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;

@Controller
class RegistrationController {

    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

//    @RequestMapping(value = "register")
//    public ModelAndView registrationForm() {
//        return new ModelAndView("login", "user", new User());
//    }

    @PostMapping(value = "register")
    public ModelAndView create(User user, @RequestParam String firstNameCreate, @RequestParam String lastNameCreate,
                               @RequestParam byte ageCreate, @RequestParam String emailCreate,
                               @RequestParam String passwordCreate, @RequestParam String roleCreate) {

        user.setId(0l);
        user.setFirstName(firstNameCreate);
        user.setLastName(lastNameCreate);
        user.setAge(ageCreate);
        user.setEmail(emailCreate);
        user.setPassword(passwordCreate);
        user.setRoles(new Role().setRoleString(roleCreate));
        userService.createUser(user);
        return new ModelAndView("redirect:/login");
    }
}
