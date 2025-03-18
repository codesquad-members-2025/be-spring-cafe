package codesquad.codestagram.controller;

import codesquad.codestagram.Article;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
    private final ArrayList<Article> articles;

    public ArticleController(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute Article article) {
        articles.add(article);
        return "redirect:/";
    }
}
