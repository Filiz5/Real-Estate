package com.team01.realestate.exception;

import com.team01.realestate.payload.response.business.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, WebRequest request) {
        // ConflictException mesajını doğrudan kullanarak hata mesajını netleştir
        //System.out.println("ConflictException: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        // Veritabanı hatası mesajı döndür
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To avoid getting a 500 Internal Server Error, a Global Exception Handler is often used in Spring MVC to catch exceptions and return responses with appropriate HTTP status codes.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseMessage<String>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest()
                .body(ResponseMessage.<String>builder()
                        .message(ex.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());
    }

    /**
     * AccessDeniedException için özel bir yapılandırma doğru görünüyor. Ancak, Spring Security'nin AccessDeniedException'ını yakalamak için kullanılan sınıf genellikle org.springframework.security.access.AccessDeniedException olur. Şu an java.nio.file.AccessDeniedException'ı yakalıyorsunuz.
         */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                HttpStatus.FORBIDDEN.value(),
//                "Access Denied",
//                request.getDescription(false) // İstek URI'sini alır
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                request.getDescription(false) // İstek URI'sini alır
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        System.out.println("MethodArgumentNotValidException: " + ex.getMessage());

        // Get the first error message
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> {
                    String fieldName = error.getField();
                    // Custom message for the phone field
                    if (fieldName.equals("phone")) {
                        return "Phone number is not valid. It should be in the format 222-333-4444";
                    } else {
                        System.out.println("Error: " + error.getDefaultMessage());
                        return error.getDefaultMessage();
                    }
                })
                .orElse("Validation error occurred");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message.trim(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }




}
