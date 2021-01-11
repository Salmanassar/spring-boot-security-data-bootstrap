package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;
import web.util.CurrentUser;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    private CurrentUser currentUser;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    public ModelAndView getListUser(Principal principal, Model model) {
        String[] selectionRoles = {"ROLE_USER", "ROLE_ADMIN"};
        ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("selectionRoles", selectionRoles);
        modelAndView.addObject("users", this.userService.listUsers());
        modelAndView.addObject("switch", currentUser.getCurrentUser(principal));
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getPageForCreating(@ModelAttribute User user) {
        return "user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "admin/create", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("id") User user) {
        return new ModelAndView("user");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @PostMapping("/create")
    public ModelAndView create(User user, @RequestParam String firstNameCreate, @RequestParam String lastNameCreate,
                               @RequestParam byte ageCreate, @RequestParam String emailCreate, @RequestParam String passwordCreate, @RequestParam String roleCreate) {

        user.setId(0L);
        user.setFirstName(firstNameCreate);
        user.setLastName(lastNameCreate);
        user.setAge(ageCreate);
        user.setEmail(emailCreate);
        user.setPassword(passwordCreate);
        user.setRoles(new Role().setRoleString(roleCreate));
        userService.createUser(user);
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") Optional<Long> id, Model model) {
        if (id.isPresent()) {
            User user = userService.getUserById(id.get());
            return new ModelAndView("user", "user", user);
        }
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public ModelAndView modifyUser(@PathVariable("id") Optional<Long> id, @RequestParam String roleFromSelection,
                                   @ModelAttribute User user, final BindingResult result) {
        try {
            user.setRoles(new Role().setRoleString(roleFromSelection));
            user = userService.updateUser(user);
        } catch (RuntimeException exception) {
            result.addError(new FieldError("user", "user", exception.getMessage()));
            return new ModelAndView("user", "user", user);
        }
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(path = "/theAdmin", method = RequestMethod.GET)
    public ModelAndView theAdminInformation(Principal principal) {
        User user = currentUser.getCurrentUser(principal);
        return new ModelAndView("tl/currentAdmin", "user", user);
    }
}

