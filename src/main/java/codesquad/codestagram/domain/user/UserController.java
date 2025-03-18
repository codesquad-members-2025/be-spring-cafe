package codesquad.codestagram.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public String showUserProfile(@PathVariable String id, Model model) {
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            // 사용자를 찾을 수 없을 때 리스트 페이지로 리다이렉트하고
            // 에러 메시지를 던달
            return "redirect:/users?error=user-not-found";
        }

        model.addAttribute("user", user);

        return "/user/profile";
    }

}
