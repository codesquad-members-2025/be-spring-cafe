package codesquad.codestagram.controller;

import codesquad.codestagram.repository.UserRepository;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        return "user/list";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{id}/form")
    public String getUpdateForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String email,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        if(!user.getPassword().equals(password)){
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/{id}/form";
        }
        userService.update(user, name, email);
        return "redirect:/users";

    }








}
