package ru.kata.spring.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.rest.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
