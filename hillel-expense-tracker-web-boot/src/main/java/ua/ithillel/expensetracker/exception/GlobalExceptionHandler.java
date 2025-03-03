package ua.ithillel.expensetracker.exception;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.ithillel.expensetracker.dto.ErrorDTO;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorDTO> handleJakartaPersistenceException(PersistenceException e) {
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundServiceException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundServiceException(NotFoundServiceException e) {
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
