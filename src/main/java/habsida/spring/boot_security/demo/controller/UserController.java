package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping
    public List<User> listUsers() {
        return userService.getAllUsers();
    }*/

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/users";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }





    @GetMapping("login")
    public String loginPage() {
        return "login";
    }
}
