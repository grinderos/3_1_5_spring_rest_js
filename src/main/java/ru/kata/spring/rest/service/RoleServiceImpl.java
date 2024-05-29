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

    @Override
    public Collection<Role> getRoles() {
        System.out.println("GET ALL ROLES");
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        System.out.println("FIND ROLE BY NAME");
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
