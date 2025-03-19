package codesquad.codestagram.user.controller;


import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.SignUpRequest;
import codesquad.codestagram.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/signUp")
    public String signUpForm() {
        return "user/form";
    }

    @PostMapping("/users/signUp")
    public String signUp(SignUpRequest request) {
        User user = new User(
                request.id(),
                request.password(),
                request.name(),
                request.email()
        );

        userService.join(user);

        return "redirect:/list";
    }
}
