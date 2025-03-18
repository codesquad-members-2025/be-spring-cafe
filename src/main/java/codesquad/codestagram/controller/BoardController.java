package codesquad.codestagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class BoardController {

    @GetMapping("/")
    public String home(Model model) {
        return "user/index";
    }
}