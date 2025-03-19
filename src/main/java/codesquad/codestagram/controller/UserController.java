package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/form")
    public String form() {
        return "user/form";
    }

    @PostMapping("/form")
    public String createForm(UserForm form) {
        User user = new User();
        user.setId(form.getId());
        user.setNickname(form.getNickname());
        user.setPassWord(form.getPassword());

        userService.join(user);

        return "redirect:/users";
    }

}
