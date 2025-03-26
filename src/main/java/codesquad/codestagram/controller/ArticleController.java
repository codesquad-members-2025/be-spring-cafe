package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articleList = articleService.findAllArticles();
        model.addAttribute("articleList", articleList);
        return "index";
    }

    @GetMapping("/qna/form")
    public String form() {
        return "qna/form";
    }

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm) {
        articleService.createArticleAndSave(articleForm);
        return "redirect:/";
    }

    @GetMapping("/qna/show")
    public String show() {
        return "qna/show";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable int index, Model model) {
        Article article = articleService.findArticleById(index);
        model.addAttribute("article", article);
        return "qna/show";
    }
}
