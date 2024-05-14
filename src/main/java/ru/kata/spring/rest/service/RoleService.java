package ru.kata.spring.rest.service;

import ru.kata.spring.rest.model.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoles();

    Role findByName(String name);

    Role save(Role role);
}
