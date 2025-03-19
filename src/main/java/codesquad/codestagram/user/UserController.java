package codesquad.codestagram.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/signUp")
    public String signUpForm() {
        return "user/form";
    }
}
