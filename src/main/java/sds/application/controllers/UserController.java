package sds.application.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sds.application.ResponseHandler;
import sds.application.config.JwtUtils;
import sds.application.request.LoginRequest;
import sds.application.request.RegisterRequest;
import sds.domain.User;
import sds.services.UserService;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class UserController {

    UserService userService;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getUser() {
        try {
            User user = userService.loadCurrentUser();
            return ResponseHandler.success(user);

        } catch (AuthorizationDeniedException e) {
            return ResponseHandler.unauthorized(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseHandler.internalError();
        }
    }

    @PostMapping("/auth/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
//
//            System.out.println("Usuario autenticado: " + authentication.getName());
//            System.out.println("Roles del usuario: " + authentication.getAuthorities());
//
//            return ResponseHandler.success("Usuario autenticado");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.createToken(authentication);
            System.out.println(authentication);
            return ResponseHandler.success(new AuthResponse(loginRequest.getUsername(), "Login exitoso", accessToken));


        } catch (AuthenticationException e) {
            return ResponseHandler.badRequest("Usuario o contraseña incorrectos");}
        catch (Exception e) {
            return ResponseHandler.internalError();
        }
    }
    record AuthResponse(
            String username,
            String message,
            String token) {}
}

