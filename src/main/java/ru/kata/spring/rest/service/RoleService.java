package ru.kata.spring.rest.service;

import ru.kata.spring.rest.model.Role;

import java.util.Collection;

public interface RoleService {
    public Collection<Role> getRoles();
    public Role findRoleByName(String name);
}
