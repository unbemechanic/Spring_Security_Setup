package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable long id){
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());

        Set<Role> userRoles = new HashSet<>();
        for (Role rId : user.getRoles()){
            Role role = userService.getRoleById(rId.getId());
            userRoles.add(role);
        }
        newUser.setRoles(userRoles);
        return ResponseEntity.ok(newUser);

    }
    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles() {
        Set<Role> allRoles = new HashSet<>(userService.getAllRoles());
        return ResponseEntity.ok(allRoles);
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> createUser(@RequestBody User user){
        Map<String, String> errors = new HashMap<>();
        if(userService.emailExists(user.getEmail())){
            errors.put("email", "Email already exists");
        }
        Set<Role> roles = new HashSet<>();
        for (Long roleId : user.getRoleIds()){
            Role role = userService.getRoleById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable long id,
            @RequestBody User user){

        Set<Role> roles = new HashSet<>();
        for (Long roleId : user.getRoleIds()){
            Role role = userService.getRoleById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        userService.updateUser(user, id);
        User updatedUser = userService.getUserById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/remove/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
