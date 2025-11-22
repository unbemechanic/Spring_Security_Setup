package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/dashboard";
    }

    @GetMapping("/add")
    public String addUserForm(Model model){
        model.addAttribute("user", new User());
        return "admin/form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "admin/form";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id){
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
