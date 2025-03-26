package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.exception.NotLoggedInException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.utility.TextUtility;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String form(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }

        return "qna/form";
    }

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }

        articleService.createArticleAndSave(loginUser, articleForm);
        return "redirect:/";
    }

    @GetMapping("/qna/show")
    public String show() {
        return "qna/show";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable int index, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }

        Article article = articleService.findArticleById(index);
        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        return "qna/show";
    }

    @DeleteMapping("/articles/{index}")
    public String delete(@PathVariable int index, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }

        Article article = articleService.findArticleById(index);
        if (article.isAuthor(loginUser)) {
            articleService.delete(article);
            return "redirect:/";
        }
        throw new UnauthorizedAccessException("본인의 게시물만 삭제할 수 있습니다.");
    }

    @GetMapping("/articles/{index}/update-form")
    public String updateForm(@PathVariable int index, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }

        Article article = articleService.findArticleById(index);
        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        return "qna/update-form";
    }

    @Transactional
    @PutMapping("/articles/{index}/update")
    public String update(@PathVariable int index, ArticleForm articleForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            throw new NotLoggedInException();
        }
        articleService.update(loginUser, index, articleForm);

        return "redirect:/articles/" + index;
    }
}
