package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String viewMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "main";
    }

    @PostMapping("/article")
    public String writeArticle(@ModelAttribute Article article) {
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String viewArticle(@PathVariable("id") Long index, Model model) {
        Article article = articleRepository.findById(index).orElseThrow();
        model.addAttribute("article", article);
        return "article/detail";
    }
}
