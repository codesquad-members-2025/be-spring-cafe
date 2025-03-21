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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public String create(UserForm userForm, RedirectAttributes redirectAttributes) {
        try{
            userService.join(userForm);
            return "redirect:/user/list";
        } catch(NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            return "redirect:/user/form";
        }
    }

    @GetMapping("/user/list")
    public String list(Model model) {
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        try{
            User user = userService.findByUserId(userId);
            model.addAttribute("user", user);
            return "user/profile";
        } catch (NoSuchElementException e){
            model.addAttribute("alertMessage", e.getMessage());
            return "index";
        }
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model) {
        try{
            User user = userService.findByUserId(userId);
            model.addAttribute("user", user);
            return "user/update-form";
        } catch (NoSuchElementException e){
            model.addAttribute("alertMessage", e.getMessage());
            return "user/list";
        }
    }

    @PutMapping("/users/{userId}/update")
    public String updateForm(UserForm userForm, Model model, RedirectAttributes redirectAttributes) {
        boolean isUpdated = userService.updateUser(userForm);
        if(isUpdated) {
            return "redirect:/users/" + userForm.getUserId();
        }
        redirectAttributes.addFlashAttribute("alertMessage", "잘못된 입력입니다.");
        return "redirect:/users/"+userForm.getUserId()+"/form";
    }
}
