package codesquad.codestagram.advice;

import codesquad.codestagram.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            ArticleNotFoundException.class,
            ReplyNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", e.getClass().getSimpleName());
        errorBody.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorBody);
    }

    @ExceptionHandler({
            InvalidPasswordException.class,
            NotLoggedInException.class
    })
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(RuntimeException e) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", e.getClass().getSimpleName());
        errorBody.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorBody);
    }

    @ExceptionHandler({
            UnauthorizedAccessException.class
    })
    public ResponseEntity<Map<String, Object>> handleForbiddenException(RuntimeException e) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", e.getClass().getSimpleName());
        errorBody.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorBody);
    }

    @ExceptionHandler({
            DuplicateUserIdException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflictException(RuntimeException e) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", e.getClass().getSimpleName());
        errorBody.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody);
    }
}

