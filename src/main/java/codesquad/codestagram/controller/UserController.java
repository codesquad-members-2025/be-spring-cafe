package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String viewUserList() {
        return "user/list";
    }
}
