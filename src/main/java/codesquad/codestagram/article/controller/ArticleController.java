package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.dto.ArticleRequest;
import codesquad.codestagram.article.service.ArticleService;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ArticleController {

    public static final String REDIRECT_LOGIN = "redirect:/users/login";
    public static final String REDIRECT_JOIN = "redirect:/users/join";
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String ARTICLE = "article";

    private final ArticleService articleService;
    private final SessionService sessionService;

    public ArticleController(ArticleService articleService, SessionService sessionService) {
        this.articleService = articleService;
        this.sessionService = sessionService;
    }

    @GetMapping("/articles/form")
    public String createForm(HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> "qna/form")
                .orElse(REDIRECT_LOGIN);
    }

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest request,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    try {
                        Long articleId = articleService.create(request, loggedInUserId);
                        return "redirect:/articles/" + articleId;
                    } catch (InvalidRequestException e) {
                        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
                        return "redirect:/articles/form";
                    }
                })
                .orElse(REDIRECT_LOGIN);
    }

    @GetMapping("/")
    public String articleList(Model model) {
        List<Article> articles = articleService.findArticles();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id,
                              Model model,
                              HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    Article article = articleService.findArticle(id);
                    model.addAttribute(ARTICLE, article);
                    return "qna/show";
                }).orElse(REDIRECT_LOGIN);
    }

    @GetMapping("/articles/{id}/form")
    public String updateForm(@PathVariable Long id,
                             Model model,
                             HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    Article article = articleService.findArticleAndVerifyOwner(id, loggedInUserId);
                    model.addAttribute(ARTICLE, article);
                    return "qna/updateForm";
                }).orElse(REDIRECT_LOGIN);
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable Long id,
                                ArticleRequest request,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    try {
                        articleService.updateArticle(id, request, loggedInUserId);
                        return "redirect:/articles/" + id;
                    } catch (InvalidRequestException e) {
                        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
                        return "redirect:/articles/" + id + "/form";
                    }
                })
                .orElse(REDIRECT_LOGIN);
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    articleService.delete(id, loggedInUserId);
                    return REDIRECT_HOME;
                }).orElse(REDIRECT_LOGIN);
    }
}
