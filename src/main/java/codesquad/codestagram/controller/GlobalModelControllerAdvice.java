package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.exception.DuplicateUserIdException;
import codesquad.codestagram.exception.InvalidPasswordException;
import codesquad.codestagram.exception.UserNotFoundException;
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

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPassword(InvalidPasswordException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFound(ArticleNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(DuplicateUserIdException.class)
    public String handleDuplicateUserId(DuplicateUserIdException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }
}
