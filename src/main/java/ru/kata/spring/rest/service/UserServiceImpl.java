package ru.kata.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.rest.exceptionProcessing.UserDaoException;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.repositories.RoleRepository;
import ru.kata.spring.rest.repositories.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserById(Long id) {
        System.out.println("find User By Id");
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    @Override
    public Collection<User> getUsers() {
        System.out.println("GET ALL USERS");
        return userRepository.findAll();
    }


    @Override
    @Transactional
    public User save(User user) {
        String enteringPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(enteringPassword));
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            user.setPassword(enteringPassword);
            throw new UserDaoException(user, ex.getMessage());
        }
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        User loadedUserFromDB;
        if ((loadedUserFromDB = findByUsername(user.getUsername())) != null &&
                !loadedUserFromDB.getId().equals(user.getId())) {
            throw new UserDaoException(user, "Такое имя пользователя уже занято");
        }
        loadedUserFromDB = findUserById(user.getId());
        String enteringPassword = user.getPassword();
        if (user.getPassword() == null ||
                enteringPassword.equals(loadedUserFromDB.getPassword()) ||
                enteringPassword.length() == 0) {
            user.setPassword(loadedUserFromDB.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            user.setPassword(enteringPassword);
            throw new UserDaoException(user, ex.getMessage());
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }


    @Transactional
    public void fillUsers() {
        fillRoles();
        User admin = new User("admin", "admin_name", "admin_lastname",
                "admin@mail.com", 33, "admin");
        admin.setRoles(new HashSet<>(roleRepository.findAll()));
        User user = new User("user", "user_name", "user_lastname",
                "user@mail.com", 22, "user");
        user.addRole(roleRepository.findByName("ROLE_USER"));
        User loadedUserFromDB = findByUsername(admin.getUsername());
        if (loadedUserFromDB == null) {
            save(admin);
        }
        loadedUserFromDB = findByUsername(user.getUsername());
        if (loadedUserFromDB == null) {
            save(user);
        }
    }

    @Transactional
    public void fillRoles() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_USER"));
        }
    }
}

