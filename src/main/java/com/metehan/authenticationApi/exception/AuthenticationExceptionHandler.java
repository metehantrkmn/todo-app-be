package com.metehan.authenticationApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> handleAuthenticationRegisterException(UserAlreadyExistException e){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        //"1. create payload containing exception details
        AuthenticationException exception = new AuthenticationException(
                e.getMessage(),
                e,
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. return response entity
        return new ResponseEntity<>(exception,status);
    }

    @ExceptionHandler(value={UsernameNotFoundException.class})
    public ResponseEntity<Object> handleAuthenticationUserNameNotFoundException(UsernameNotFoundException e){

        HttpStatus status = HttpStatus.NOT_FOUND;

        //"1. create payload containing exception details
        AuthenticationException exception = new AuthenticationException(
                e.getMessage(),
                e,
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. return response entity
        return new ResponseEntity<>(exception,status);
    }


}
