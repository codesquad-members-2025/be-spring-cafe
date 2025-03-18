package codesquad.codestagram.controller;

import org.springframework.ui.Model;
import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showRegisterForm() {
        return "user/form";
    }

    @PostMapping
    public String registerUser(@ModelAttribute UserRequestDto dto) {
        userService.registerUser(dto);
        return "redirect:/users";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserResponseDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable String id, Model model) {
        UserResponseDto user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, HttpSession session) {
        System.out.println("[DEBUG] 로그인 시도: ID=" + id + ", Password=" + password);

        UserResponseDto user = userService.authenticate(id, password);
        if (user == null) {
            System.out.println("[DEBUG] 로그인 실패: 잘못된 ID 또는 비밀번호");
            return "redirect:/users/login?error=true";
        }

        session.setAttribute("loginUser", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
