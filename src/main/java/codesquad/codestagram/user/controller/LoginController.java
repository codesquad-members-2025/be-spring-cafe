package codesquad.codestagram.user.controller;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.LoginRequest;
import codesquad.codestagram.user.service.SessionService;
import codesquad.codestagram.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static codesquad.codestagram.user.service.UserService.USER_NOT_FOUND;

@Controller
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(LoginRequest request,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<User> userOptional = userService.authenticate(request.userId(), request.password());
        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", USER_NOT_FOUND);
            return "redirect:/users/login";
        }
        sessionService.login(session, userOptional.get());
        return "redirect:/";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        sessionService.logout(session);
        return "redirect:/";
    }
}
