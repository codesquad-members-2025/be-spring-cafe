package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public String create(UserForm userForm) {
        User user = new User(userForm.getUserId(), userForm.getName(), userForm.getPassword(),userForm.getEmail());
        userService.join(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String list(Model model) {
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        Optional<User> findUser= userService.findByUserId(userId);
        if(findUser.isPresent()) {
            model.addAttribute("user", findUser.get());
            return "user/profile";
        }
        return "index";
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model) {
        Optional<User> foundUser = userService.findByUserId(userId);
        if(foundUser.isPresent()) {
            model.addAttribute("user", foundUser.get());
            return "user/update-form";
        }
        return "user/list";
    }

    @PostMapping("/user/update")
    public String updateForm(UserForm userForm, Model model) {
        String userId = userForm.getUserId();
        Optional<User> foundUser = userService.findByUserId(userId);
        if(foundUser.isPresent()) {
            User user = foundUser.get();
            if(userForm.getPassword().equals(user.getPassword())) {
                user.setName(userForm.getName());
                user.setPassword(userForm.getPassword());
                user.setEmail(userForm.getEmail());
                return "redirect:/users/" + userId;
            }
            return "redirect:/users/" + userId + "/form";
        }
        return "redirect:/user/list";
    }
}
