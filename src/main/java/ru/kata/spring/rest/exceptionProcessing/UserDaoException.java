package ru.kata.spring.rest.exceptionProcessing;

import ru.kata.spring.rest.model.User;

public class UserDaoException extends RuntimeException {
    private final User user;
    private final String message;

    public UserDaoException(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
