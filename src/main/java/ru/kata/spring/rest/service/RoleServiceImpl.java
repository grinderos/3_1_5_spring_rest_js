package ru.kata.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.repositories.RoleRepository;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Collection<Role> getRoles() {
        return roleRepository.findAll();
    }
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
