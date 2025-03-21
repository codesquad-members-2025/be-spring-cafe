package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm) {
        try{
            articleService.createArticleAndSave(articleForm);
        } catch (NoSuchElementException e){
            String message = "오류가 발생했습니다. " + e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable int index, Model model) {
        try{
            Article article = articleService.findArticleById(index);
            model.addAttribute("article", article);
            return "qna/show";
        } catch(NoSuchElementException e){
            String message = "오류가 발생했습니다. " + e.getMessage();
            return "redirect:/";
        }
    }
}
