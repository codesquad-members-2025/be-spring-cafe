package codesquad.codestagram.common.exception;

import codesquad.codestagram.domain.article.exception.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.exception.ReplyNotFoundException;
import codesquad.codestagram.domain.user.exception.DuplicatedUserException;
import codesquad.codestagram.domain.user.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleAuthenticationException(UnauthorizedException ex,
                                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException ex,
                                                 RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex,
                                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/users?error=user-not-found";
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public String handleDuplicatedUserException(DuplicatedUserException ex,
                                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/users?error=duplicated-user";
    }

    @ExceptionHandler(ReplyNotFoundException.class)
    public String handleReplyNotFoundException(ReplyNotFoundException ex,
                                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/";
    }

}
