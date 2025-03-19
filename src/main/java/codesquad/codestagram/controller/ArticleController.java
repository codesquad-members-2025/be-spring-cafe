package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.repository.ArticleRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute ArticleDto.ArticleRequestDto requestDto) {
        Article article = requestDto.toArticle();
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "/home";
    }

    @GetMapping("articles/{articleId}")
    public String showArticleDetail(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId).get();
        model.addAttribute(article);
        return "articles/show";
    }
}
