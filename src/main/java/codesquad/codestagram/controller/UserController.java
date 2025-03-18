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
}
