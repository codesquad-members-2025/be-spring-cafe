package codesquad.codestagram.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public String signUp(@RequestParam String id,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        User user = new User(id, password, name, email);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("")
    public String index(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "/user/list";
    }

    @GetMapping("{id}")
    public String showUserProfile(@PathVariable String id, Model model) {
        User user = userRepository.findById(id);

        if (user == null) {
            // 사용자를 찾을 수 없을 때 리스트 페이지로 리다이렉트하고
            // 에러 메시지를 던달
            return "redirect:/users?error=user-not-found";
        }

        model.addAttribute("user", user);

        return "/user/profile";
    }

    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable String id, Model model) {
        User user = userRepository.findById(id);

        if (user == null) {
            return "redirect:/users?error=user-not-found";
        }

        model.addAttribute("user", user);

        return "/user/edit";
    }

    @PostMapping("{id}/update")
    public String updateUser(@PathVariable String id,
                             @RequestParam String email,
                             @RequestParam String name,
                             @RequestParam String currentPassword,
                             @RequestParam String newPassword) {
        User user = userRepository.findById(id);

        if (user == null) {
            return "redirect:/users?error=user-not-found";
        }

        if (!user.isMatchPassword(currentPassword)) {
            return "redirect:/users/" + id + "/form?error=wrong-password";
        }

        user.setEmail(email);
        user.setName(name);
        user.setPassword(newPassword);

        return "redirect:/users/" + id;
    }

}
