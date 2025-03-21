package codesquad.codestagram.domain.auth;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.user.User;
import codesquad.codestagram.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RequestMapping("/auth")
@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return "redirect:/auth/login?error=login-failed";
        }

        if (user.get().isMatchPassword(password)) {
            session.setAttribute(SessionConstants.USER_SESSION_KEY, user.get());
            return "redirect:/";
        } else {
            return "redirect:/auth/login?error=login-failed";
        }
    }

    @PostMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SessionConstants.USER_SESSION_KEY);
        return "redirect:/";
    }

}
