package com.metehan.todoApi.exception;

public class TodoAlreadyExistsException extends  RuntimeException{

    public TodoAlreadyExistsException(String message) {
        super(message);
    }

    public TodoAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
