package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    @ModelAttribute
    public void addLoginUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user != null) {
            model.addAttribute("loginUser", user);
        }
    }
}
