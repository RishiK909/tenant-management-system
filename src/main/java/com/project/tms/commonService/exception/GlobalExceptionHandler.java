package com.project.tms.commonService.exception;

import com.project.tms.commonService.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice Catch exception globally everywhere in the app.
 * No need to handle in try catch
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @logger : it displays the error in console
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * @param ex This is used to throw exception when @Valid fails
     *           This is mostly from the user end else frontend like userName,email
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        logger.error("Validation failed: ", ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.badRequest().body(new ApiResponse<>(message, false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>("You do not have permission to access this resource", false));
    }

    /**
     * This is mostly due to developer else server error
     * so log is important
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        logger.error("Something went wrong: ", ex);
        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>("Something went wrong", false));
    }
}
