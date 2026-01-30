package eu.martin.store.common;

import eu.martin.store.cart.ItemNotFoundException;
import eu.martin.store.cart.ProductException;
import eu.martin.store.cart.QuantityExceedException;
import eu.martin.store.checkout.CartException;
import eu.martin.store.email.MailException;
import eu.martin.store.users.DuplicateUserException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
class GlobalExceptionHandler {
    /*@ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<Map<String, String>> handleHandlerMethodValidation2(HandlerMethodValidationException exception) {
        var errors = new HashMap<String, String>();

        for (var validationResult : exception.getParameterValidationResults()) {
            validationResult.getResolvableErrors().forEach(messageSourceResolvable -> System.out.println("arguments: " + Arrays.toString(messageSourceResolvable.getArguments())));
            validationResult.getResolvableErrors().forEach(messageSourceResolvable -> System.out.println("default message: " + messageSourceResolvable.getDefaultMessage()));
            System.out.println("------");
        }


        return ResponseEntity.badRequest().body(errors);
    }*/
/*
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidationException2(HandlerMethodValidationException ex) {
        // Create the Problem Detail object
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Failed"
        );

        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle("Method Argument Validation Failed");

        // Extract errors
        var errors = ex.getParameterValidationResults().stream()
                .flatMap(res -> res.getResolvableErrors().stream()
                        .map(err -> res.getMethodParameter().getParameterName() + " " + err.getDefaultMessage()))
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }
*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MailException.class)
    ResponseEntity<String> handleMail(MailException ex) {
        return ResponseEntity.internalServerError().body("error: " + ex.getClass() + ": " + ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({ItemNotFoundException.class, DuplicateUserException.class, QuantityExceedException.class, CartException.class, ProductException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class, PropertyReferenceException.class})
    ResponseEntity<ErrorMessage> handleBadRequests(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
