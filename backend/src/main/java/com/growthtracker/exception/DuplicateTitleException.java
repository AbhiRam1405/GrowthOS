package com.growthtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTitleException extends RuntimeException {
    public DuplicateTitleException(String title) {
        super("A task with the title '" + title + "' already exists.");
    }
}
