package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/user/form")
    public String userForm() {
        return "user/form";
    }

    @PostMapping("/user/create")
    public String create(UserForm userForm, RedirectAttributes redirectAttributes) {
        try{
            userService.join(userForm);
            return "redirect:/user/list";
        } catch(IllegalStateException e) {
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
    public String showUpdateForm(@PathVariable String userId, Model model, HttpSession session) {
        try{
            User loginUser = (User) session.getAttribute("loginUser");
            if(loginUser == null) {
                return "redirect:/";
            }
            User user = userService.findByUserId(userId);
            if(loginUser.equals(user)){
                model.addAttribute("user", user);
                return "user/update-form";
            }
            return "redirect:/user/form";
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

    @GetMapping("/user/login")
    public String showLoginPage(Model model) {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session) {
        try {
            User user = userService.userLogin(userId, password);
            session.setAttribute("loginUser", user);
            return "redirect:/users/" + userId;
        }
        catch (NoSuchElementException e){
            return "redirect:/user/login_failed";
        }
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
