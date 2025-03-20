package codesquad.codestagram.domain.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    public String signUp(@RequestParam String userId,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        User user = new User(userId, password, name, email);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("")
    public String index(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "user/list";
    }

    @GetMapping("{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            // 사용자를 찾을 수 없을 때 리스트 페이지로 리다이렉트하고
            // 에러 메시지를 던달
            return "redirect:/users?error=user-not-found";
        }

        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return "redirect:/users?error=user-not-found";
        }

        model.addAttribute("user", user);

        return "user/edit";
    }

    @PutMapping("{id}")
    @Transactional
    public String updateUser(@PathVariable Long id,
                             @RequestParam String email,
                             @RequestParam String name,
                             @RequestParam String currentPassword,
                             @RequestParam String newPassword,
                             HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/error";
        }

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/users?error=user-not-found";
        }

        if (!sessionUser.equals(user)) {
            return "redirect:/error";
        }

        if (!user.isMatchPassword(currentPassword)) {
            return "redirect:/users/" + id + "/form?error=wrong-password";
        }

        user.setEmail(email);
        user.setName(name);
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }

        return "redirect:/users/" + id;
    }

}
