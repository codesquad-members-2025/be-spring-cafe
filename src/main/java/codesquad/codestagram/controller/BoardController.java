package codesquad.codestagram.controller;

import codesquad.codestagram.dto.UserResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

    @GetMapping("/")
    public String home(Model model) {
        return "user/index";
    }


    @GetMapping("/questions")
    public String questions(Model model, HttpSession session) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        String writerName = (loginUser != null) ? loginUser.getName() : "익명";
        model.addAttribute("writerName", writerName);
        return "qna/form";
    }

}