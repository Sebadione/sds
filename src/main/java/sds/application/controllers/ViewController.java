package sds.application.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('USER')")
    public String hello(Model model) {
        model.addAttribute("message", "Hello World!");
        return "welcome";
    }
}
