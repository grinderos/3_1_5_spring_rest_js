package ru.kata.spring.rest.service;

import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;

import java.util.Collection;

public interface UserService {
    public User findByUsername(String username);
    public User findUserById(Long id);
    public Collection<User> getUsers();
    public User save(User user);
    public User update(User user);
    public void deleteUserById(Long id);

}
