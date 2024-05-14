package ru.kata.spring.rest.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.repositories.RoleRepository;

@Component
public class RolesCheckerImpl implements RolesChecker {
    RoleRepository roleRepository;

    public RolesCheckerImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void checkRoles(User user, Authentication auth) {
        if (user.getRoles().isEmpty() &&
                user.getUsername().equals(auth.getName()) &&
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            user.addRole(roleRepository.findByName("ROLE_ADMIN"));
        } else if (user.getRoles().isEmpty()) {
            user.addRole(roleRepository.findByName("ROLE_USER"));
        }
    }
}

