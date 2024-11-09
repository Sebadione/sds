package sds.services;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sds.domain.Role;
import sds.domain.User;
import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserService implements UserDetailsService {

    Map<String, User> users = new HashMap<>();
    PasswordEncoder passwordEncoder;

    public UserService(@Autowired PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        User user = new User("seba", passwordEncoder.encode("1234"), "seba@gmail.com", Role.ROLE_USER);
        User admin = new User("admin", passwordEncoder.encode("1234"), "admin@gmail.com", Role.ROLE_ADMIN);

        users.put(user.getUsername(), user);
        users.put(admin.getUsername(), admin);
    }

    @Override
    public User loadUserByUsername(String username) {
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("User not found");
        }
        return users.get(username);
    }

    public User register(String username, String password, String mail) {
        boolean userExists = users.containsKey(username);
        if (userExists) {
            throw new DataIntegrityViolationException("The User already exists.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, mail, Role.ROLE_USER);
        return users.put(username, user);
    }

    public User loadCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return loadUserByUsername(username);
    }
}
