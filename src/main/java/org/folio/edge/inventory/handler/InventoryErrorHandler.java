package org.folio.edge.inventory.handler;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.folio.inventory.domain.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class InventoryErrorHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException exception) {
    Error errorResponse = buildErrorResponse(400, exception.getMessage());
    return ResponseEntity.status(errorResponse.getCode())
        .body(errorResponse);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<Error> handleFeignException(FeignException exception) {
    String properErrorMessage = exception.contentUTF8();
    Error errorResponse = buildErrorResponse(exception.status(), properErrorMessage);
    return ResponseEntity.status(exception.status())
        .body(errorResponse);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException exception) {
    Error errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
        .body(errorResponse);
  }

  private Error buildErrorResponse(int status, String message) {
    log.error("Error occurred during service chain call, {}", message);
    Error errorResponse = new Error();
    errorResponse.setCode(status);
    errorResponse.setErrorMessage(message);
    return errorResponse;
  }

}
