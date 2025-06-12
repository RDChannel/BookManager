package rd.book.management.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class BookExceptionHandler {//Code to handle any errors

    @ExceptionHandler(IllegalArgumentException.class)// this code is for any incorrect input data
    public ResponseEntity<String> exception(IllegalArgumentException ex) {
        if (!"Not Found".equals(ex.getMessage())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)// this code is for any validation errors
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null)
            ? fieldError.getField() + ": " + fieldError.getDefaultMessage()
            : "Invalid request payload";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
     
}
