package codesquad.codestagram.controller;

import codesquad.codestagram.service.CommentService;
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
    private final CommentService commentService;

    public UserController(UserService userService
                        , CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
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
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user/change";
    }



    @PostMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam(required = false) Long boardId) {
        commentService.deleteComment(id);
        return (boardId != null) ? "redirect:/boards/" + boardId : "redirect:/";
    }
}
