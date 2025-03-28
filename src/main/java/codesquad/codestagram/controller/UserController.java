package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public String create(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email) {
        User user = new User(userId, name, password, email);

        userService.join(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String list(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/user/{userId}")
    public String profile(@PathVariable("userId") String userId, Model model) {
        User user = userService.findOneUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model) {
        User user = userService.findOneUser(userId).get();
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/users/{userId}/update")
    public String update(
            @PathVariable String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email) {
        User user = userService.findOneUser(userId).get();
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);

        userService.join(user);
        return "redirect:/users";
    }

}
