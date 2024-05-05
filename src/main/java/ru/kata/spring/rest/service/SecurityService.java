package ru.kata.spring.rest.service;

public interface SecurityService {
    boolean isAuthenticated();

    void autoLogin(String username, String password);
}