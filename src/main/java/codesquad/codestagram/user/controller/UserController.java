package codesquad.codestagram.user.controller;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.SignUpRequest;
import codesquad.codestagram.user.dto.UserUpdateRequest;
import codesquad.codestagram.user.service.SessionService;
import codesquad.codestagram.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUpRequest request,
                         RedirectAttributes redirectAttributes) { // @ModelAttribute 공부,
        try {
            userService.join(request);
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/users/join";
        }
    }

    @GetMapping
    public String showUserList(Model model) {

        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{userSeq}")
    public String showUserProfile(@PathVariable Long userSeq, Model model) {
        User user = userService.findUser(userSeq);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{userSeq}/verify")
    public String verifyPasswordForm(@PathVariable Long userSeq,
                                     HttpSession session,
                                     Model model) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(userSeq);

        if (!loggedInUser.getSeq().equals(findUser.getSeq()) || !userSeq.equals(findUser.getSeq())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        userService.findUser(userSeq);
        model.addAttribute("seq", userSeq);
        return "user/passwordVerifyForm";
    }

    @PostMapping("/{userSeq}/verify-password")
    public String verifyPassword(@PathVariable Long userSeq,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(userSeq);

        if (!loggedInUser.getSeq().equals(findUser.getSeq()) || !userSeq.equals(findUser.getSeq())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }
        try {
            boolean isVerified = userService.verifyPassword(userSeq, password);

            if (isVerified) {
                return "redirect:/users/" + userSeq + "/form";
            }
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/" + userSeq + "/verify";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + userSeq + "/verify";
        }
    }

    @GetMapping("/{userSeq}/form")
    public String updateUserProfileForm(@PathVariable Long userSeq,
                                        Model model,
                                        HttpSession session) {
        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(userSeq);

        if (!loggedInUser.getSeq().equals(findUser.getSeq()) || !userSeq.equals(findUser.getSeq())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        User user = userService.findUser(userSeq);
        model.addAttribute("user", user);
        return "user/updateForm";
    }


    @PutMapping("/{userSeq}")
    public String update(@PathVariable Long userSeq,
                         @ModelAttribute UserUpdateRequest request,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        User findUser = userService.findUser(userSeq);

        if (!loggedInUser.getSeq().equals(findUser.getSeq()) || !userSeq.equals(findUser.getSeq())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        try {
            userService.updateUser(request.toEntity(userSeq));

            User updatedUser = userService.findUser(userSeq);
            sessionService.login(session, updatedUser);

            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + userSeq + "/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/users/" + userSeq + "/form";
        }
    }
}
