package kg.attractor.bookingsaas.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.bookingsaas.exceptions.*;
import kg.attractor.bookingsaas.exceptions.body.ValidationExceptionBody;
import kg.attractor.bookingsaas.service.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AppExceptionHandler {
    private final ErrorService errorService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ValidationExceptionBody handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        return errorService.handleValidationException(ex, request);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class, SecurityException.class})
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(RuntimeException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.FORBIDDEN)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(RuntimeException e) {
        if (e instanceof IllegalArgumentException) {
            return handleIllegalArgument(e);
        } else {
            return handleNoSuchElementException(e);
        }
    }

    private static ResponseEntity<ExceptionResponse> handleIllegalArgument(RuntimeException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private static ResponseEntity<ExceptionResponse> handleNoSuchElementException(RuntimeException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ExceptionResponse> handleHttpClientErrorExceptionConflict(HttpClientErrorException.Conflict e) {
        return handleIllegalArgument(e);
    }
}