package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.dto.ArticleRequest;
import codesquad.codestagram.article.service.ArticleService;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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
    public String createForm(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        return "qna/form";
    }

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest articleRequest,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        try {
            Long articleId = articleService.create(articleRequest, loggedInUserId);
            return "redirect:/articles/" + articleId;
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/form";
        }
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
                              HttpServletRequest request) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Article article = articleService.findArticle(id);
        model.addAttribute(ARTICLE, article);
        return "qna/show";
    }

    @GetMapping("/articles/{id}/form")
    public String updateForm(@PathVariable Long id,
                             Model model,
                             HttpServletRequest request) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        Article article = articleService.findArticleAndVerifyOwner(id, loggedInUserId);
        model.addAttribute(ARTICLE, article);
        return "qna/updateForm";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable Long id,
                                ArticleRequest articleRequest,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        try {
            articleService.updateArticle(id, articleRequest, loggedInUserId);
            return "redirect:/articles/" + id;
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/" + id + "/form";
        }
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id,
                         HttpServletRequest request) {

        HttpSession session = request.getSession();

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        articleService.delete(id, loggedInUserId);
        return REDIRECT_HOME;
    }
}
