package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public User register(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found!")
        );
    }
}
