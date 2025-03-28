package codesquad.codestagram.common.exception;

import codesquad.codestagram.domain.article.exception.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.user.exception.DuplicatedUserException;
import codesquad.codestagram.domain.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleAuthenticationException(UnauthorizedException ex,
                                                HttpServletResponse response,
                                                Model model) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        model.addAttribute("errorMessage", ex.getMessage());

        return "redirect:/error";
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException ex,
                                                 HttpServletResponse response,
                                                 Model model) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("errorMessage", ex.getMessage());

        return "redirect:/error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex,
                                              HttpServletResponse response,
                                              Model model) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("errorMessage", ex.getMessage());

        return "redirect:/users?error=user-not-found";
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public String handleDuplicatedUserException(DuplicatedUserException ex,
                                                HttpServletResponse response,
                                                Model model) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        model.addAttribute("errorMessage", ex.getMessage());

        return "redirect:/users?error=duplicated-user";
    }

}
