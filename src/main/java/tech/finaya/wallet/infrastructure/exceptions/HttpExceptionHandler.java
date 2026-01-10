package tech.finaya.wallet.infrastructure.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.finaya.wallet.domain.exceptions.WalletDoesntExistException;
import tech.finaya.wallet.domain.exceptions.WalletKeyAlreadyExistException;
import tech.finaya.wallet.domain.exceptions.WalletTypeKeyAlreadyExistException;

@RestControllerAdvice
public class HttpExceptionHandler {

    @Autowired
    private Logger log;

    @ExceptionHandler(WalletDoesntExistException.class)
    public ResponseEntity<ApiError> handleNotFound(
            WalletDoesntExistException ex,
            HttpServletRequest request) {

        log.warn("Not found: {}", ex.getMessage());

        ApiError err = new ApiError();

        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        err.setMessage(ex.getMessage());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        KeyTypeInvalidException.class,
        WalletTypeKeyAlreadyExistException.class,
        WalletKeyAlreadyExistException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(
            Exception ex,
            HttpServletRequest request) {

        log.info("Bad request: {}", ex.getMessage());

        ApiError err = new ApiError();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        err.setMessage(ex.getMessage());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error", ex);

        ApiError err = new ApiError();
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        err.setMessage("An unexpected error occurred. Please contact support.");
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
