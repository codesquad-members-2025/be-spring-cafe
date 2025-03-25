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
    public String handleInvalidPassword(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "로그인에 실패했습니다.");
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "유저를 찾을 수 없습니다.");
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFound(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "게시물을 찾을 수 없습니다.");
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(DuplicateUserIdException.class)
    public String handleDuplicateUserId(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "이미 동일한 아이디의 사용자가 있습니다.");
        return "redirect:" + request.getHeader("Referer");
    }
}
