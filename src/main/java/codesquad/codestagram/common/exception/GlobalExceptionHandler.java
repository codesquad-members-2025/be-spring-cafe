package codesquad.codestagram.common.exception;

import codesquad.codestagram.domain.article.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.UnauthorizedException;
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

        return "error";
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException ex,
                                                HttpServletResponse response,
                                                Model model) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }

}
