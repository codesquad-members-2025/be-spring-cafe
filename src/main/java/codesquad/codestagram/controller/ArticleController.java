package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/questions")
    public String ask(
            @RequestParam("writer") String writer,
            @RequestParam("title") String title,
            @RequestParam("contents") String contents) {
        Article article = new Article();
        article.setWriter(writer);
        article.setTitle(title);
        article.setContents(contents);

        articleService.ask(article);

        return "redirect:/";
    }

}
