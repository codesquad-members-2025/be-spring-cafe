package codesquad.codestagram.controller;

import org.springframework.ui.Model;
import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String login(@RequestParam String id, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto user = userService.authenticate(id, password);
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 틀립니다.");
            return "redirect:/users/login";
        }

        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/{id}/revise")
    public String showEditUserForm(@PathVariable String id, Model model) {
        UserResponseDto user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users"; // 사용자가 없으면 리스트 페이지로 리디렉션
        }
        model.addAttribute("user", user);
        return "user/change"; // user/edit.html로 이동
    }

    @PutMapping("/{id}/revise")
    public String updateUser(@PathVariable String id, @RequestParam String name, @RequestParam String email) {
        userService.updateUser(id, name, email);
        return "redirect:/users";
    }
}
