package codesquad.codestagram.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final List<User> users;

    public UserController() {
        this.users = new ArrayList<>();
    }

    @PostMapping("")
    public String signUp(@RequestParam String id,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        User user = new User(id, password, name, email);
        users.add(user);

        return "redirect:/users";
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("users", users);
        return "/user/list";
    }

}
