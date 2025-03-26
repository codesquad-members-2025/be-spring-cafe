package codesquad.codestagram.controller;

import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.userrepository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String users(Model model){
        List<User> users =  userRepository.findAll();
        model.addAttribute("users",users);
        return "user/list";
    }

    @PostConstruct
    public void init() {
        userRepository.save(new User("a","a","a@a","bazzi"));
        userRepository.save(new User("b","b","b@b","bazzi1"));
    }
}
