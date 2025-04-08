package codesquad.codestagram.user.controller;

import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.common.exception.error.ResourceNotFoundException;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.LoginRequest;
import codesquad.codestagram.user.service.SessionService;
import codesquad.codestagram.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static codesquad.codestagram.article.controller.ArticleController.*;


@Controller
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;
    private final SessionService sessionService;

    public LoginController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        try {
            User user = userService.authenticate(loginRequest.userId(), loginRequest.password());
            sessionService.login(session, user.getId());

            String redirectUrl = sessionService.popRedirectUrl(session);
            return "redirect:" + redirectUrl;
        } catch (InvalidRequestException | ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/users/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        sessionService.logout(session);
        return REDIRECT_HOME;
    }

}
