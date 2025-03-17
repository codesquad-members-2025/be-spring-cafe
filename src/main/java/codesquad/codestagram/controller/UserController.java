package codesquad.codestagram.controller;

import codesquad.codestagram.User;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
}
