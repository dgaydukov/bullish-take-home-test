package bullish.electronic.store.aop;

import bullish.electronic.store.exception.RequestValidationException;
import bullish.electronic.store.model.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequestValidationException.class)
    protected ResponseEntity<Object> handleConflict(
            RequestValidationException ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        return handleExceptionInternal(ex, new ResponseError(ex.getLocalizedMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}