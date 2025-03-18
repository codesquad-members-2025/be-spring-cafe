package codesquad.codestagram.controller;

import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 가입 폼 페이지
    @GetMapping("/form")
    public String showUserForm() {
        return "user/form";
    }

    // 회원 가입 처리
    @PostMapping()
    public String registerUser(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("loginId") String loginId,
    @RequestParam("password") String password){
        userRepository.saveUser(name, email,loginId,password);
        return "redirect:/users";
    }

    // 회원 목록 조회
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    // 특정 회원 프로필 조회
    @GetMapping("/{userId}")
    public String userProfile(@PathVariable("userId") Long userId, Model model) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return "redirect:/users"; // 존재하지 않는 사용자 처리
        }
        model.addAttribute("user", user);
        return "user/profile";
    }
}