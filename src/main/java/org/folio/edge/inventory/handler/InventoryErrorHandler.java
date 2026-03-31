package org.folio.edge.inventory.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.folio.inventory.domain.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Log4j2
@RestControllerAdvice
public class InventoryErrorHandler {

  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<Error> handleRestClientResponseException(HttpStatusCodeException exception) {
    var properErrorMessage = exception.getResponseBodyAsString();
    var status = exception.getStatusCode().value();
    var errorResponse = buildErrorResponse(status, properErrorMessage, exception);
    return ResponseEntity.status(exception.getStatusCode())
        .body(errorResponse);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Error handleExceptionWithBadRequestStatus(RuntimeException exception) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Error handleEntityNotFoundException(EntityNotFoundException exception) {
    return buildErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), exception);
  }

  private Error buildErrorResponse(int status, String message, Exception exception) {
    log.error("Not valid request cause, {}", message);
    log.debug(message, exception);
    var errorResponse = new Error();
    errorResponse.setCode(status);
    errorResponse.setErrorMessage(message);
    return errorResponse;
  }

}
