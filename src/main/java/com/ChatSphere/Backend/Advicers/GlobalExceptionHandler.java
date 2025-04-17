package com.ChatSphere.Backend.Advicers;

import com.ChatSphere.Backend.Exceptions.EmailAlreadyExistsException;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Exceptions.IncorrectPasswordException;
import com.ChatSphere.Backend.Exceptions.OAuthSignInRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException exp) {
        return new ResponseEntity<>(exp.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException exp) {
        return new ResponseEntity<>(exp.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException exp) {
        return new ResponseEntity<>(exp.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OAuthSignInRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleOauthSignInException(OAuthSignInRequiredException exp) {
        return new ResponseEntity<>(exp.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
