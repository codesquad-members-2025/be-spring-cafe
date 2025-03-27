package codesquad.codestagram.controller;

import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("loginUser")
    public User loginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }

    @GetMapping
    public String users(Model model){
        List<User> users =  userService.findAll();
        List<UserResponseDto> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(new UserResponseDto(user));
        }

        model.addAttribute("users",dtos);
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

    @GetMapping("/{id}")
    public String userInfo(@PathVariable Long id, Model model){
        User user = userService.getUserById(id);
        UserResponseDto dto = new UserResponseDto(user);
        model.addAttribute("user", dto);
        return "user/profile";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userid,
                        @RequestParam String password,
                        HttpSession session){
        User user = userService.login(userid, password);
        session.setAttribute("loginUser", user);
        return "redirect:/users"; //리다이렉트는 다르게 써야함
    }//로그인 실패 로직 구현 -> 예외 핸들러로 보내면되나?

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}