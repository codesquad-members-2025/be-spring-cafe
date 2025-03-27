package codesquad.codestagram.domain.user;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.auth.UnauthorizedException;
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
        try {
            userService.signUp(userId, password, name, email);
        } catch (UnauthorizedException e) {
            return "redirect:/users?error=duplicated-user";
        }

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
        try {
            User user = userService.getUserProfile(id);
            model.addAttribute("user", user);
        } catch (UserNotFoundException e) {
            return "redirect:/users?error=user-not-found";
        }

        return "user/profile";
    }

    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            User user = userService.getUserProfile(id);
            model.addAttribute("user", user);
        } catch (UserNotFoundException e) {
            return "redirect:/users?error=user-not-found";
        }

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
        try {
            userService.updateUser(id, email, name, currentPassword, newPassword, sessionUser);
        } catch (UserNotFoundException e) {
            return "redirect:/users?error=user-not-found";
        } catch (DuplicatedUserException e) {
            return "redirect:/users?error=duplicated-user";
        }

        return "redirect:/users/" + id;
    }

}
