package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userRepository.existsUserByLoginId(user.getLoginId())) {
           redirectAttributes.addFlashAttribute("message","이미 존재하는 아이디입니다.");
           return "redirect:/users/form";
        }
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping
    public String viewUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{id}/form")
    public String viewUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, @RequestParam String confirmPassword
        , RedirectAttributes redirectAttributes) {
        User existingUser = userRepository.findById(id).orElseThrow();

        if (existingUser.getPassword().equals(confirmPassword)) {
            userRepository.save(user);
            return "redirect:/users";
        }
        redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
        return "redirect:/users/" + id + "/form";
    }

}
