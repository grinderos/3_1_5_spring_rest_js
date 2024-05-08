package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.UserDetailsServiceImpl;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private UserDetailsServiceImpl userService;

    @Autowired
    public RestApiController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers() {
        System.out.println("\nfindAll -->\n"+ResponseEntity.ok(userService.getUsers()));
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        System.out.println("\ngetUser -->\n"+ResponseEntity.ok(userService.findUserById(id)));
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(Authentication auth) {
        System.out.println("\ngetCurrentUser -->\n"+ResponseEntity.ok(userService.findByUsername(auth.getName())));
        return ResponseEntity.ok(userService.findByUsername(auth.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user, BindingResult bindingResult) {
        System.out.println("\nNew User:\n"+user);
        userService.save(user);
        System.out.println("\nadd -->\n"+ResponseEntity.ok(userService.findByUsername(user.getUsername())));
        return ResponseEntity.ok(userService.findByUsername(user.getUsername()));
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user, BindingResult bindingResult) {
        System.out.println("\nGiven User:\n"+user);
        userService.update(user);
        System.out.println("\nupdate -->\n"+ResponseEntity.ok(userService.findUserById(user.getId())));
        return ResponseEntity.ok(userService.findUserById(user.getId()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping( "/findAllRoles")
    public ResponseEntity<List<Role>> findAllRoles() {
        System.out.println("\nfindAllRoles -->\n"+ResponseEntity.ok(userService.getRoles()));
        return ResponseEntity.ok(userService.getRoles());
    }

//    @GetMapping("/admin/{id}")
//    public User getUserInfo(@PathVariable("id") Long id) {
////        User thisUser = userService.findByUsername(principal.getName());
////        model.addAttribute("thisUser", thisUser);
//        return userService.findUserById(id);
//    }


//    @GetMapping("/admin")
//    public String adminPage(Model model, Authentication auth) {
////        User thisUser = userService.findByUsername(auth.getName());
////        model.addAttribute("thisUser", thisUser);
//        model.addAttribute("newUser", new User());
//        model.addAttribute("allRoles", userService.getRoles());
//        model.addAttribute("allUsers", userService.getUsers());
//        return "admin/admin_panel";
//    }
//
//    @PostMapping("/new")
//    public String add(@ModelAttribute("newUser") User newUser) {
//        if (newUser.getRoles().isEmpty()) {
//            newUser.addRole(userService.findRoleByName("ROLE_USER"));
//        }
//        if (!userService.save(newUser)) {
//            System.out.println("\nПользователь с логином '" +
//                    newUser.getUsername() + "' уже существует\n");
//        }
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/update")
//    public String updateUser(@ModelAttribute("user") User user, Authentication auth, HttpSession session) {
//
//        if (user.getRoles().isEmpty() &&
//                user.getUsername().equals(auth.getName()) &&
//                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            user.addRole(userService.findRoleByName("ROLE_ADMIN"));
//        } else if (user.getRoles().isEmpty()) {
//            user.addRole(userService.findRoleByName("ROLE_USER"));
//        }
//        if (!userService.update(user)) {
//            return "redirect:/admin";
//        }
//        if (userService.findByUsername(auth.getName()) == null) {
//            session.invalidate();
//            return "redirect:/login";
//        }
//        return auth.getAuthorities()
//                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ?
//                "redirect:/admin" : "redirect:/user";
//    }
//
//
//    @PostMapping("/delete")
//    public String deleteUser(@RequestParam("id") Long id, Authentication auth, HttpSession session) {
//        User deletedUser = userService.findUserById(id);
//        userService.deleteUserById(id);
//        if (deletedUser.getUsername().equals(auth.getName())) {
//            session.invalidate();
//            return "redirect:/";
//        }
//        return "redirect:/admin";
//    }
}
