package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserDto;
import codesquad.codestagram.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    public static final String WRONG_PASSWORD = "비밀번호가 틀렸습니다.";
    private final ArrayList<User> users;
    private final UserRepository userRepository;

    public UserController(ArrayList<User> users, UserRepository userRepository) {
        this.users = users;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public String signUp(@ModelAttribute UserDto.UserRequestDto requestDto) {
        User user = requestDto.toUser();
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> all = userRepository.findAll();
        model.addAttribute("users", all);
        return "user/list";
    }

    @GetMapping("/users/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "user/profile";
    }

    @GetMapping("/users/{id}/update")
    public String editUser(Model model, @PathVariable int id) {
        User user = getUserById(id);
        model.addAttribute("user", user);
        return "user/updateForm";  // 정보 수정 페이지로 이동
    }

    @PostMapping("/users/verify_password")
    public String verifyPassword(@RequestParam String password, @RequestParam int id, Model model) {
        User user = getUserById(id);
        model.addAttribute("user", user);

        if (user.getPassword().equals(password)) {
            model.addAttribute("passwordValid", true);
        }else {
            model.addAttribute("passwordValid", false);
            model.addAttribute("error", WRONG_PASSWORD);
        }

        return "user/updateForm"; // 실패 시 프로필 화면으로 돌아가기
    }

    @PutMapping("users/update")
    public String updateProfile(@RequestParam String name, @RequestParam String email, @RequestParam int id, Model model) {
        User user = getUserById(id);
        user.updateUser(name, email);

        model.addAttribute("user", user);
        return "redirect:/users";
    }

    private User getUserById(int id) {
        return users.get(id -1);
    }

}
