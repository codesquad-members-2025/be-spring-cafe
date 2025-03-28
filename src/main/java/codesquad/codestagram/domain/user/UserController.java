package codesquad.codestagram.domain.user;

import codesquad.codestagram.common.constants.SessionConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public String signUp(@RequestParam String userId,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        userService.signUp(userId, password, name, email);

        return "redirect:/users";
    }

    @GetMapping("")
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "user/list";
    }

    @GetMapping("{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserProfile(id);
        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        User user = userService.getUserProfile(id);
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
        User sessionUser = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        userService.updateUser(id, email, name, currentPassword, newPassword, sessionUser);

        return "redirect:/users/" + id;
    }

}
