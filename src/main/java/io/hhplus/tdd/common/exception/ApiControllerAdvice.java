package io.hhplus.tdd.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), errorCode.getMessage());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("error", "errorMessage"));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ignored) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            "에러가 발생했습니다."));
  }
}
