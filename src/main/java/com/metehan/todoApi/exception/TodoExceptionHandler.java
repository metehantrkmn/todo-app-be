package com.metehan.todoApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class TodoExceptionHandler {


    @ExceptionHandler(value = {TodoAlreadyExistsException.class})
    public ResponseEntity<Object> handleTodoAlreadyExistsException(TodoAlreadyExistsException e){
        HttpStatus status = HttpStatus.CONFLICT;

        TodoException exception = new TodoException(
                e.getMessage(),
                e.getCause(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception,status);
    }

    @ExceptionHandler(value = {NoTodosExistException.class})
    public ResponseEntity<Object> handleNoTodosExistException(NoTodosExistException e){
        HttpStatus status = HttpStatus.CONFLICT;

        TodoException exception = new TodoException(
                e.getMessage(),
                e.getCause(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception,status);
    }

}
