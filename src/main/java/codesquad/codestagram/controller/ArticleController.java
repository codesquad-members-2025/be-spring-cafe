package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public String writeArticle(@ModelAttribute Article article) {
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/{index}")
    public String viewArticle(@PathVariable("index") Long index, Model model) {
        Article article = articleRepository.findById(index).orElseThrow();
        model.addAttribute("article", article);
        return "article/detail";
    }
}
