package codesquad.codestagram.controller;

import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String users(Model model){
        List<User> users =  userService.findAll();
        model.addAttribute("users",users);
        return "user/list";
    }

    public void init() {
        userService.initExampleUsers();
    }

    @GetMapping("/signup")
    public String addForm(){
        return "user/form";
    }

    @PostMapping("/signup")
    public String addUser(@ModelAttribute("user") UserRequestDto dto){
        userService.join(dto);
        return "redirect:/users";
    }
}
