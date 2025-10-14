package com.restaurapp.restaurapp.domain.exception;

public class ExistRecordException extends  RuntimeException {
    private String message;

    public ExistRecordException(String message) {
        super(message);
    }
}
