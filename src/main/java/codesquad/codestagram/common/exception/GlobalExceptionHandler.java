package codesquad.codestagram.common.exception;

import codesquad.codestagram.common.exception.dto.ErrorResponse;
import codesquad.codestagram.common.exception.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                e.getHttpStatus()
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public String handleDuplicateResourceException(DuplicateResourceException e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                e.getHttpStatus()
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException(ForbiddenException e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                e.getHttpStatus()
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                e.getHttpStatus()
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }

    @ExceptionHandler(InvalidRequestException.class)
    public String handleInvalidRequestException(InvalidRequestException e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                e.getHttpStatus()
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        model.addAttribute(ERROR, errorResponse);
        return ERROR;
    }
}
