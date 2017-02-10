package edu.asu.diging.gilesecosystem.freddie.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(SearchQueryException.class)
    public ResponseEntity<Object> catchSearchException(SearchQueryException ex, WebRequest request) {
        logger.error("Search query threw error.", ex);
        return handleExceptionInternal(ex, new Error("Could not execute query.", ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
