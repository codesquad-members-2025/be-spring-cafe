package codesquad.codestagram.user.controller;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.SignUpRequest;
import codesquad.codestagram.user.dto.UserUpdateRequest;
import codesquad.codestagram.user.service.UserService;
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

    @PostMapping("/users/signUp")
    public String signUp(@ModelAttribute SignUpRequest request) { // @ModelAttribute 공부
        User user = new User(
                request.userId(),
                request.password(),
                request.name(),
                request.email()
        );

        userService.join(user);

        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/users/{userSeq}")
    public String showUserProfile(@PathVariable Long userSeq, Model model) {
        User user = userService.findUser(userSeq);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{userSeq}/form")
    public String updateUserProfileForm(@PathVariable Long userSeq, Model model) {
        User user = userService.findUser(userSeq);
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/users/{userSeq}")
    public String updateUserProfile(@PathVariable Long userSeq, @ModelAttribute UserUpdateRequest request) {
        userService.updateUser(userSeq, request);
        return "redirect:/users";
    }
}
