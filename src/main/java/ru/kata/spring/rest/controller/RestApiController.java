package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private UserService userService;
    private RoleService roleService;
    private RolesChecker rolesChecker;

    @Autowired
    public RestApiController(UserService userService, RolesChecker rolesChecker, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.rolesChecker = rolesChecker;
    }

    @GetMapping("/user/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(Authentication auth) {
        return ResponseEntity.ok(userService.findByUsername(auth.getName()));
    }

    @GetMapping("/admin/getUsers")
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/admin/get/{id}")
    public User get(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/admin/add")
    public User add(@RequestBody User user, Authentication auth) {
        rolesChecker.checkRoles(user, auth);
        return userService.save(user);
    }

    @PutMapping("/admin/update")
    public User update(@RequestBody User user, Authentication auth) {
        rolesChecker.checkRoles(user, auth);
        return userService.update(user);
    }

    @DeleteMapping("/admin/delete/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/admin/findAllRoles")
    public Collection<Role> findAllRoles() {
        return roleService.getRoles();
    }
}
