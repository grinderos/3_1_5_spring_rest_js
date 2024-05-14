package ru.kata.spring.rest.service;

import org.springframework.security.core.Authentication;
import ru.kata.spring.rest.model.User;

public interface RolesChecker {
    void checkRoles(User user, Authentication auth);
}
