package ru.kata.spring.rest.service;

import ru.kata.spring.rest.model.User;

import java.util.Collection;

public interface UserService {
    User findByUsername(String username);

    User findUserById(Long id);

    Collection<User> getUsers();

    User save(User user);

    User update(User user);

    void deleteUserById(Long id);

}
