package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    @ModelAttribute
    public void addLoginUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user != null) {
            model.addAttribute("loginUser", user);
        }
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            InvalidPasswordException.class,
            ArticleNotFoundException.class,
            DuplicateUserIdException.class,
            NotLoggedInException.class,
            UnauthorizedAccessException.class
    })
    public String handleCommonExceptions(RuntimeException  e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }
}
