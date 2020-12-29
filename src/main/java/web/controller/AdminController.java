package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.User;
import web.service.UserService;
import web.util.Checkbox;
import web.util.CurrentUser;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    private Checkbox checkbox;

    @Autowired
    private CurrentUser currentUser;

    public AdminController(UserService userService) {
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

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    public ModelAndView getListUser(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("tl/list");
        modelAndView.addObject("users", this.userService.listUsers());
        modelAndView.addObject("switch", currentUser.getCurrentUser(principal));
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String getPageForCreating(@ModelAttribute User user) {
        return "tl/form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "admin/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("id") User user) {
        return new ModelAndView("tl/list");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid User user, @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                               @RequestParam(name = "isUser", required = false) boolean isUser, final BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        try {
            checkbox.selectRoleFromCheckbox(user, isAdmin, isUser);
            userService.createUser(user);
        } catch (RuntimeException ex) {
            result.addError(new FieldError("user", "user", ex.getMessage()));
            return new ModelAndView("tl/form", "user", user);
        }
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            User user = userService.getUserById(id.get());
            return new ModelAndView("tl/formUpdate", "user", user);
        }
        return new ModelAndView("redirect:/admin");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN ROLE_USER')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public ModelAndView modifyUser(@PathVariable("id") Optional<Long> id, @ModelAttribute User user,
                                   @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                                   @RequestParam(name = "isUser", required = false) boolean isUser,
                                   final BindingResult result) {
        try {
            checkbox.selectRoleFromCheckbox(user, isAdmin, isUser);
            userService.updateUser(user);
        } catch (RuntimeException exception) {
            result.addError(new FieldError("user", "user", exception.getMessage()));
            return new ModelAndView("tl/formUpdate", "user", user);
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

