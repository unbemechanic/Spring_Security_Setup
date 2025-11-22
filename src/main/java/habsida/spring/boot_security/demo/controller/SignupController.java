package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User()); // <-- this is required
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        userService.register(user);
        return "redirect:/login";
    }
}

