package codesquad.codestagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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






}
