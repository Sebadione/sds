package sds.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sds.domain.Role;
import sds.domain.User;
import sds.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    public User register(String username, String password, String mail) {
        boolean userExists = userRepository.findByUsername(username).isPresent();
        if (userExists) {
            throw new DataIntegrityViolationException("The User already exists.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, mail, Role.USER);
        return userRepository.save(user);
    }

    public User loadCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return loadUserByUsername(username);
    }
}
