package ru.kata.spring.rest.exceptionProcessing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserDaoExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleUserDataIntegrityViolation(UserDaoException ex) {
        return new ResponseEntity<>(ex.getUser(), HttpStatus.BAD_REQUEST);
    }
}
