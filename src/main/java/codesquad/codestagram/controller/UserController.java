package codesquad.codestagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class UserController {

    private static final ArrayList<User> userList = new ArrayList<>();

    @PostMapping("/users")
    public String addUser(@ModelAttribute User user) {
        userList.add(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users", userList);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable String userId, Model model){
        for (User user : userList) {
            if(user.getUserId().equals(userId)) model.addAttribute("user", user);
        }
        return "user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String getUpdateForm(@PathVariable String userId, Model model){
        for (User user : userList) {
            if(user.getUserId().equals(userId)) model.addAttribute("user", user);
        }
        return "user/updateForm";
    }

    @PostMapping("/users/{userId}/update")
    public String updateUser(@PathVariable String userId,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String email,
                             RedirectAttributes redirectAttributes){

        for (User user : userList) {
            if(user.getUserId().equals(userId)){
                if(!user.getPassword().equals(password)){
                    redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
                    return "redirect:/users/{userId}/form";
                }
                user.setName(name);
                user.setEmail(email);
            }
        }
        return "redirect:/users";
    }








}
