package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

import static codesquad.codestagram.config.AppConstants.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(user);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "이미 사용중인 아이디입니다.");
            return "redirect:/users/form";
        }

        return "redirect:/";
    }

    @GetMapping
    public String getUserList(Model model) {
        List<User> users = userService.findUserList();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            User user = userService.findUser(id);
            model.addAttribute("user", user);

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.getMessage());
            return "redirect:/";
        }

        return "user/profile";
    }

    @GetMapping("/{id}/form")
    public String getUpdateForm(@PathVariable("id") Long id, Model model, HttpSession session
        , RedirectAttributes redirectAttributes) {

        User loginUser = (User) session.getAttribute(LOGIN_USER);

        try {
            User findUser = userService.findUser(id);

            if (loginUser.getId() != findUser.getId()) { //URL을 직접 입력해서 타인의 회원정보를 수정할 경우
                redirectAttributes.addFlashAttribute(ALERT_MESSAGE, "사용자 ID가 일치하지 않습니다.");
                return "redirect:/";
            }
            model.addAttribute("user", findUser);

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(ALERT_MESSAGE, e.getMessage());
            return "redirect:/";
        }

        return "user/updateForm";
    }

    @PutMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, @RequestParam String confirmPassword
        , RedirectAttributes redirectAttributes) {

        boolean updateResult = userService.updateUser(user, id, confirmPassword);

        if (!updateResult) {
            redirectAttributes.addFlashAttribute(MESSAGE, "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/" + id + "/form";
        }

        return "redirect:/users/" + id;
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String loginId, @RequestParam String password, HttpSession session
        , RedirectAttributes redirectAttributes) {

        Map<String, Object> result = userService.authenticateUser(loginId, password);

        if (!(boolean)result.get(SUCCESS)) {
            redirectAttributes.addFlashAttribute(MESSAGE, result.get(MESSAGE));
            return "redirect:/users/loginForm";
        }

        session.setAttribute(LOGIN_USER, result.get("user"));
        return "redirect:/";


    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
