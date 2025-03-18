package codesquad.codestagram.controller;

import codesquad.codestagram.User;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    public static final String WRONG_PASSWORD = "비밀번호가 틀렸습니다.";
    private final ArrayList<User> users;

    public UserController(ArrayList<User> users) {
        this.users = users;
    }

    @PostMapping("/users")
    public String signUp(@ModelAttribute User user) {
        users.add(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String showUserProfile(@PathVariable int userId, Model model) {
        User user = users.get(userId-1);
        model.addAttribute("user", user);
        model.addAttribute("id", userId);
        return "user/profile";
    }

    @GetMapping("/users/{userId}/update")
    public String editUser(Model model, @PathVariable int userId) {
        User user = users.get(userId - 1);
        model.addAttribute("user", user);
        return "user/updateForm";  // 정보 수정 페이지로 이동
    }

    @PostMapping("/users/verify_password")
    public String verifyPassword(@RequestParam String password, @RequestParam int id, Model model) {
        User user = users.get(id-1);
        model.addAttribute("user", user);

        if (user.getPassword().equals(password)) {
            model.addAttribute("passwordValid", true);
        }else {
            model.addAttribute("passwordValid", false);
            model.addAttribute("error", WRONG_PASSWORD);
        }

        return "user/updateForm"; // 실패 시 프로필 화면으로 돌아가기
    }

}
