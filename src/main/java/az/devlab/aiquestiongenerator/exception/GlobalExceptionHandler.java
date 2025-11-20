package az.devlab.aiquestiongenerator.exception;

import az.devlab.aiquestiongenerator.dto.ApiErrorResponse;
import az.devlab.aiquestiongenerator.dto.FieldValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex,
                                                           HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex,
                                                             HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex,
                                                               HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenException ex,
                                                            HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleAiService(AiServiceException ex,
                                                            HttpServletRequest request) {
        log.error("AI service error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "AI service is temporarily unavailable. Please try again later.",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex,
                                                                    HttpServletRequest request) {
        return buildResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomValidation(ValidationException ex,
                                                                   HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {

        List<FieldValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        String message = "Validation failed for one or more fields.";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                      HttpServletRequest request) {

        List<FieldValidationError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(this::mapConstraintViolation)
                .collect(Collectors.toList());

        String message = "Validation failed for one or more constraints.";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                         HttpServletRequest request) {
        String message = "Malformed JSON request.";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                     HttpServletRequest request) {
        String message = "HTTP method not supported for this endpoint.";
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message, request.getRequestURI(), null);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex,
                                                          HttpServletRequest request) {
        log.error("Unexpected error", ex);
        String message = "An unexpected error occurred.";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI(), null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                                                           String message,
                                                           String path,
                                                           List<FieldValidationError> fieldErrors) {

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();

        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

    private FieldValidationError mapFieldError(FieldError fe) {
        return FieldValidationError.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build();
    }

    private FieldValidationError mapConstraintViolation(ConstraintViolation<?> cv) {
        String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
        return FieldValidationError.builder()
                .field(field)
                .message(cv.getMessage())
                .build();
    }
}
