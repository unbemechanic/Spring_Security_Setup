/*
package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/user")
    public String user(Authentication authentication, Model model) {
        // Pass logged-in user to Thymeleaf template
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user/user";
    }


}
*/
