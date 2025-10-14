package com.restaurapp.restaurapp.config;

import com.restaurapp.restaurapp.domain.exception.CustomError;
import com.restaurapp.restaurapp.domain.exception.ExistRecordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    private static final String AN_ERROR_OCCURRED_PLEASE_CONTACT_THE_ADMINISTRATOR = "Error en el servidor";

    private static final ConcurrentHashMap<String, Integer> STATUS_CODE = new ConcurrentHashMap<>();

    public ErrorHandler() {
        STATUS_CODE.put(RuntimeException.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        STATUS_CODE.put(ExistRecordException.class.getSimpleName(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomError> handleAllExceptions(Exception exception) {
        ResponseEntity<CustomError> result;

        String exceptionName = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        Integer code = STATUS_CODE.get(exceptionName);

        if (code != null) {
            CustomError customError = new CustomError(exceptionName, message);
            result = new ResponseEntity<>(customError, HttpStatus.valueOf(code));
        } else {
            CustomError customError = new CustomError(exceptionName, AN_ERROR_OCCURRED_PLEASE_CONTACT_THE_ADMINISTRATOR);
            result = new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}
