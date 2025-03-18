package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    List<User> users = new ArrayList<>();

    @PostMapping
    public String signup(@ModelAttribute User user) {
        users.add(user);
        return "redirect:/users";
    }

    @GetMapping
    public String viewUserList(Model model) {
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable("id") String id, Model model) {
        for (User user : users) {
            if (user.getUserId().equals(id)) {
                model.addAttribute("user", user);
            }
        }
        return "user/profile";
    }
}
