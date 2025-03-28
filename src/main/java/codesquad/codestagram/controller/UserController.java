package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String SESSION_LOGIN_USER = "loginUser";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(user);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("message", "이미 사용중인 아이디입니다.");
            return "redirect:/users/form";
        }

        return "redirect:/users";
    }

    @GetMapping
    public String viewUserList(Model model) {
        List<User> users = userService.getUserList();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            User user = userService.getUser(id);
            model.addAttribute("user", user);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        }
        return "user/profile";
    }

    @GetMapping("/{id}/form")
    public String viewUpdateForm(@PathVariable("id") Long id, Model model, HttpSession session
        , RedirectAttributes redirectAttributes) {


        User loginUser = (User) session.getAttribute(SESSION_LOGIN_USER);

        if (loginUser == null) { //로그인 상태인지 확인
            return "redirect:/users/loginForm";
        }

        try {
            User findUser = userService.getUser(id);

            if (loginUser.getId() != findUser.getId()) { //URL을 직접 입력해서 타인의 회원정보를 수정할 경우
                throw new IllegalArgumentException("사용자 ID가 일치하지 않습니다.");
            }
            model.addAttribute("user", findUser);

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            return "redirect:/";
        }

        return "user/updateForm";
    }

    @PutMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, @RequestParam String confirmPassword
        , RedirectAttributes redirectAttributes) {

        boolean updateResult = userService.updateUser(user, id, confirmPassword);

        if (!updateResult) {
            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/" + id + "/form";
        }

        return "redirect:/users/" + id;
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session
        , RedirectAttributes redirectAttributes) {

        try {
            User findUser = userService.authenticate(loginId, password);

            if (findUser == null) {
                redirectAttributes.addFlashAttribute("message", "비밀번호가 틀렸습니다.");
                return "redirect:/users/loginForm";
            }

            session.setAttribute(SESSION_LOGIN_USER, findUser);
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users/loginForm";
        }

    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
