package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Registering a new user with default ROLE_USER
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);
        return userRepository.save(user);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void setRolesToUser(User user, List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        if(roleIds != null) {
            for(Long id : roleIds) {
                roleRepository.findById(id).ifPresent(roles::add);
            }
        }
        user.setRoles(roles);
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }


    // CRUD operations for User entity
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void saveUser(User user) {

        // Editing existing user
        if (user.getId() != null) {

            User existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                // keep old password
                user.setPassword(existing.getPassword());
            } else {
                // encode new password
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

        } else {
            // Creating new user
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty for new users");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found!")
        );
    }
}
