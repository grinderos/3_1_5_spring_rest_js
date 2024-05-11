package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.UserDetailsServiceImpl;
import ru.kata.spring.rest.service.UserValidator;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private UserDetailsServiceImpl userService;
    private UserValidator userValidator;

    @Autowired
    public RestApiController(UserDetailsServiceImpl userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/user/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(Authentication auth) {
        return ResponseEntity.ok(userService.findByUsername(auth.getName()));
    }

    @GetMapping("/admin/getUsers")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/admin/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("/admin/add")
    public ResponseEntity<User> add(@RequestBody User user, Authentication auth) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(user);
        }
        userValidator.checkRoles(user, auth);
        userService.save(user);
        return ResponseEntity.ok(userService.findByUsername(user.getUsername()));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<User> update(@RequestBody User user, Authentication auth) {
        userValidator.checkRoles(user, auth);
        if (!userService.update(user)) {
            return ResponseEntity.badRequest().body(user);
        }
        return ResponseEntity.ok(userService.findUserById(user.getId()));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/findAllRoles")
    public ResponseEntity<Collection<Role>> findAllRoles() {
        return ResponseEntity.ok(userService.getRoles());
    }
}
