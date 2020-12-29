package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;
import web.util.Checkbox;

import javax.validation.Valid;

@Controller
class RegistrationController {

    private UserService userService;
    private Checkbox checkbox;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setCheckbox(Checkbox checkbox) {
        this.checkbox = checkbox;
    }

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registration", "user", new User());
    }

    @PostMapping(value = "register")
    public ModelAndView createUser(@Valid final User user, @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                                   @RequestParam(name = "isUser", required = false) boolean isUser, final BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", user);
        }
        try {
            checkbox.selectRoleFromCheckbox(user, isAdmin, isUser);
            userService.createUser(user);
        } catch (Exception e) {
            result.addError(new FieldError("user", "user", e.getMessage()));
            return new ModelAndView("registration", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }
}
