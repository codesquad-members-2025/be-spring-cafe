package codesquad.codestagram.user.controller;

import codesquad.codestagram.common.exception.error.DuplicateResourceException;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.SignUpRequest;
import codesquad.codestagram.user.dto.UserUpdateRequest;
import codesquad.codestagram.user.service.SessionService;
import codesquad.codestagram.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static codesquad.codestagram.article.controller.ArticleController.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUpRequest signUpRequest,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        try {
            User newUser = userService.join(signUpRequest);
            sessionService.login(session, newUser.getId());
            return REDIRECT_HOME;
        } catch (DuplicateResourceException | InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return REDIRECT_JOIN;
        }
    }

    @GetMapping
    public String showUserList(Model model) {

        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{id}/verify")
    public String verifyPasswordForm(@PathVariable Long id,
                                     Model model,
                                     HttpServletRequest request) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        User user = userService.findByIdAndVerifyOwner(id, loggedInUserId);
        model.addAttribute("id", user.getId());
        return "user/passwordVerifyForm";
    }

    @PostMapping("/{id}/verify-password")
    public String verifyPassword(@PathVariable Long id,
                                 @RequestParam String password,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        boolean isVerified = userService.verifyPassword(id, password, loggedInUserId);

        if (!isVerified) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/" + id + "/verify";
        }
        return "redirect:/users/" + id + "/form";
    }

    @GetMapping("/{id}/form")
    public String updateUserProfileForm(@PathVariable Long id,
                                        Model model,
                                        HttpServletRequest request) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        User user = userService.findByIdAndVerifyOwner(id, loggedInUserId);
        model.addAttribute("user", user);
        return "user/updateForm";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute UserUpdateRequest userUpdateRequest,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();

        try {
            userService.updateUser(id, userUpdateRequest, loggedInUserId);
            return "redirect:/users";
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/users/" + id + "/form";
        }
    }
}
