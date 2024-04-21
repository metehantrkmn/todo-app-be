package com.metehan.todoApi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class TodoException {

    private String message;
    private Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

}
