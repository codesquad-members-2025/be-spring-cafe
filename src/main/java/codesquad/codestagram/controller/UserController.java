package codesquad.codestagram.controller;

import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String registerUser(@Validated @ModelAttribute("User") User user) {
        String name = user.getName();
        String email = user.getEmail();
        String loginId = user.getLoginId();
        String password = user.getPassword();
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
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return "redirect:/users"; // 존재하지 않는 사용자 처리
        }
        model.addAttribute("user", user.get());
        return "user/profile";
    }

    @PostMapping("/{userId}/form")
    public String updateUserProfile(@PathVariable("userId") Long userId, @RequestParam("password") String password,
                                    @RequestParam("name") String name, @RequestParam("email") String email, HttpSession session) {

        // 세션에서 로그인한 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인하지 않은 경우
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 로그인한 사용자가 본인의 정보만 수정할 수 있도록 처리
        if (!loginUser.getId().equals(userId)) {
            return "redirect:/users"; // 본인 외의 사용자는 수정할 수 없도록 처리
        }

        // 프로필 업데이트
        userRepository.updateUserProfile(userId, password, name, email);
        return "redirect:/users/" + userId; // 수정된 프로필로 리다이렉트
    }
}