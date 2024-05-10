package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.rest.service.UserDetailsServiceImpl;


@Controller
public class UserController {

    private UserDetailsServiceImpl userService;

    @Autowired
    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUserInfo() {
        return "/user";
    }

    @GetMapping("/admin")
    public String getAdminPanel(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "/admin/admin_panel";
        } else return "redirect:/user";
    }

    @GetMapping("/fillUsers")
    public String fillUsers() {
        userService.fillUsers();
        return "redirect:/";
    }

    @GetMapping("/fillRoles")
    public String fillRoles() {
        userService.fillRoles();
        return "redirect:/";
    }

    @GetMapping("/truncate")
    public String truncate() {
        userService.truncate();
        return "redirect:/";
    }
}