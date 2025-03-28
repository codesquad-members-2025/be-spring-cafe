package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.utility.TextUtility;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/qna/write-form")
    public String form(HttpSession session) {
        return "qna/form";
    }

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        articleService.createArticleAndSave(loginUser, articleForm);
        return "redirect:/";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable int index, Model model, HttpSession session) {
        Article article = articleService.findArticleById(index);
        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        return "qna/show";
    }

    @DeleteMapping("/articles/{index}")
    public String delete(@PathVariable int index, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Article article = articleService.findArticleIfOwner(loginUser, index);
        articleService.delete(article);
        return "redirect:/";
    }

    @GetMapping("/articles/{index}/update-form")
    public String updateForm(@PathVariable int index, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Article article = articleService.findArticleIfOwner(loginUser, index);

        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        return "qna/update-form";
    }

    @PutMapping("/articles/{index}/update")
    public String update(@PathVariable int index, ArticleForm articleForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        articleService.update(loginUser, index, articleForm);

        return "redirect:/articles/" + index;
    }
}
