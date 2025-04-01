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

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findUser(id);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{id}/verify")
    public String verifyPasswordForm(@PathVariable Long id,
                                     HttpSession session,
                                     Model model) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(id);

        if (!loggedInUser.getId().equals(findUser.getId()) || !id.equals(findUser.getId())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        userService.findUser(id);
        model.addAttribute("id", id);
        return "user/passwordVerifyForm";
    }

    @PostMapping("/{id}/verify-password")
    public String verifyPassword(@PathVariable Long id,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(id);

        if (!loggedInUser.getId().equals(findUser.getId()) || !id.equals(findUser.getId())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }
        try {
            boolean isVerified = userService.verifyPassword(id, password);

            if (isVerified) {
                return "redirect:/users/" + id + "/form";
            }
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/" + id + "/verify";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + id + "/verify";
        }
    }

    @GetMapping("/{id}/form")
    public String updateUserProfileForm(@PathVariable Long id,
                                        Model model,
                                        HttpSession session) {
        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        User findUser = userService.findUser(id);

        if (!loggedInUser.getId().equals(findUser.getId()) || !id.equals(findUser.getId())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        User user = userService.findUser(id);
        model.addAttribute("user", user);
        return "user/updateForm";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute UserUpdateRequest request,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        User findUser = userService.findUser(id);

        if (!loggedInUser.getId().equals(findUser.getId()) || !id.equals(findUser.getId())) {
            model.addAttribute("errorMessage", "다른 사용자의 정보는 수정할 수 없습니다.");
            return "error";
        }

        try {
            userService.updateUser(request.toEntity(id));

            User updatedUser = userService.findUser(id);
            sessionService.login(session, updatedUser);

            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + id + "/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/users/" + id + "/form";
        }
    }
}
