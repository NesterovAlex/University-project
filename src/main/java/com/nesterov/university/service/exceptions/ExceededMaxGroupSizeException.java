package com.nesterov.university.service.exceptions;

public class ExceededMaxGroupSizeException extends RuntimeException{

    public ExceededMaxGroupSizeException(String message) {
        super(message);
    }
}
