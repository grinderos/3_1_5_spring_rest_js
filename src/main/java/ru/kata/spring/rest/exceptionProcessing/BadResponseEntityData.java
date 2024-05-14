package ru.kata.spring.rest.exceptionProcessing;

import org.springframework.validation.FieldError;
import ru.kata.spring.rest.model.User;

import java.util.Collections;
import java.util.List;

public class BadResponseEntityData {
    private final User user;
    private List<FieldError> fieldErrors;
    private final String message;

    public BadResponseEntityData(User user, List<FieldError> fieldErrors, String message) {
        this(user, message);
        this.fieldErrors = fieldErrors;
        System.out.println("UserValidationData constructor");
        System.out.println(message);
    }

    public BadResponseEntityData(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors == null ? Collections.emptyList() : fieldErrors;
    }

    public String getMessage() {
        return message;
    }
}
