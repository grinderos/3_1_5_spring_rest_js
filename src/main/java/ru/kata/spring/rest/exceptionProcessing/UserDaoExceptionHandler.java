package ru.kata.spring.rest.exceptionProcessing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserDaoExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleUserDataIntegrityViolation(UserDaoException ex) {
        System.out.println("ApiExceptionHandler handleUserDataIntegrityViolation");
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new BadResponseEntityData(ex.getUser()
                , "Такое имя пользователя уже занято"), HttpStatus.BAD_REQUEST);
    }
}
