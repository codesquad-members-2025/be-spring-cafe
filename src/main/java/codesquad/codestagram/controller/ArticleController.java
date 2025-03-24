package codesquad.codestagram.controller;


import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {


    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public ArticleController(ArticleRepository articleRepository, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute Article article){
        articleService.save(article);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showArticles(Model model){

        List<Article> articleList = articleService.findAll();
        model.addAttribute("articles", articleList);
        return "article/index";
    }


    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article/show";
    }


}
