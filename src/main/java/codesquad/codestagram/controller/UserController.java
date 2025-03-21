package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
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

    @PostMapping
    public String signup(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping
    public String viewUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "user/updateForm";
    }
}
