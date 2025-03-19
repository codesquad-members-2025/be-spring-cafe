package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm) {
        Optional<User> user = userService.findByUserId(articleForm.getUserId());
        if(user.isPresent()) {
            Article article = new Article(user.get(),articleForm.getTitle(),articleForm.getContent());
            articleService.saveArticle(article);
        }
        return "redirect:/";
    }
}
