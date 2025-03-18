package codesquad.codestagram.controller;

import codesquad.codestagram.Article;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/")
    public String showArticles(Model model) {
        model.addAttribute("articles", articles);
        return "/home";
    }

    @GetMapping("articles/{articleId}")
    public String showArticleDetail(@PathVariable int articleId, Model model) {
        Article article = articles.get(articleId - 1);
        model.addAttribute(article);
        return "articles/show";
    }
}
