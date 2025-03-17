package codesquad.codestagram.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping
    private String index() {
        return "index";
    }

    @GetMapping("/login")
    private String login() {
        return "login";
    }

    @GetMapping("/signup")
    private String signup() {
        return "signup";
    }

}
