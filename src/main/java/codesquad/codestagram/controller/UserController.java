package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.InvalidPasswordException;
import codesquad.codestagram.exception.NotLoggedInException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/form")
    public String userForm() {
        return "user/form";
    }

    @PostMapping("/user/create")
    public String create(UserForm userForm, RedirectAttributes redirectAttributes) {
        userService.join(userForm);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String list(Model model) {
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }
        User user = userService.findByUserId(userId);
        if(loginUser.equals(user)){
            model.addAttribute("user", user);
            return "user/update-form";
        }
        throw new UnauthorizedAccessException("다른 유저의 정보는 수정할 수 없습니다.");
    }

    @PutMapping("/users/{userId}/update")
    public String updateForm(UserForm userForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }
        userService.updateUser(loginUser, userForm);
        return "redirect:/users/" + loginUser.getUserId();
    }

    @GetMapping("/user/login")
    public String showLoginPage() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        User user = userService.userLogin(userId, password);
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("loginUser", user);
        return "redirect:/users/" + userId;
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
