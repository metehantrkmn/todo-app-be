package com.metehan.todoApi.exception;

public class NoTodosExistException extends RuntimeException {

    public NoTodosExistException(String message) {
        super(message);
    }

    public NoTodosExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
