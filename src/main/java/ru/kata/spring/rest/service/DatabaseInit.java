package ru.kata.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInit {

    private final UserServiceImpl userService;

    @Autowired
    public DatabaseInit(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        userService.fillUsers();
    }
}
