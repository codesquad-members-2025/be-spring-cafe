package codesquad.codestagram.domain.auth;

import codesquad.codestagram.domain.user.User;
import codesquad.codestagram.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return "redirect:/auth/login?error=login-failed";
        }

        if (user.isMatchPassword(password)) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            return "redirect:/login?error=login-failed";
        }
    }

    @PostMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

}
