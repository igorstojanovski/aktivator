package io.aktivator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataException extends RuntimeException {
    public DataException(String message) {
        super(message);
    }

    public DataException(Exception e) {
        super(e);
    }
}
