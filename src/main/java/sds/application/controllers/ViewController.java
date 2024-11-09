package sds.application.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
    @PreAuthorize("isAuthenticated()")
    public String welcome(Model model) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("message", "Página Principal");
        return "welcome";
    }
}


