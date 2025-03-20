package codesquad.codestagram.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest request) {
        Article article = new Article(
                request.getWriter(),
                request.getTitle(),
                request.getContent()
        );

        articleService.register(article);

        return "redirect:/";
    }

    
}
