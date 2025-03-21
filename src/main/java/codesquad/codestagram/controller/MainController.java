package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MainController {

    private final ArticleService articleService;

    public MainController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<Article> articles = articleService.findAllArticle();
        model.addAttribute("articles", articles);
        return "/qna/show";
    }

//    @GetMapping("/users")
//    public String list(Model model) {
//        List<User> users = userService.findAllUsers();
//        model.addAttribute("users", users);
//        return "/user/list";
//    }
//
//    @GetMapping("/user/{userId}")
//    public String profile(@PathVariable("userId") String userId, Model model) {
//        User user = userService.findOneUser(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
//        model.addAttribute("user", user);
//        return "/user/profile";
//    }

}
