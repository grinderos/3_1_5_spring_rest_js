package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/admin/get/{id}")
    public ResponseEntity<User> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("/admin/add")
    public ResponseEntity<User> add(@RequestBody User user, Authentication auth) {
        rolesChecker.checkRoles(user, auth);
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<User> update(@RequestBody User user, Authentication auth) {
        rolesChecker.checkRoles(user, auth);
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/findAllRoles")
    public ResponseEntity<Collection<Role>> findAllRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }
}
