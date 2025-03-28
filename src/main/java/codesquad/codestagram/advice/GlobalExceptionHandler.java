package codesquad.codestagram.advice;

import codesquad.codestagram.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserNotFoundException.class,
            InvalidPasswordException.class,
            ArticleNotFoundException.class,
            DuplicateUserIdException.class,
            NotLoggedInException.class,
            UnauthorizedAccessException.class,
            ReplyNotFoundException.class
    })
    public String handleCommonExceptions(RuntimeException  e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }
}
