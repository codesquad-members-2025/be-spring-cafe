package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.dto.ArticleRequest;
import codesquad.codestagram.article.service.ArticleService;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class ArticleController {

    public static final String REDIRECT_LOGIN = "redirect:/users/login";
    public static final String REDIRECT_JOIN = "redirect:/users/join";
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String ERROR = "error";



    private final ArticleService articleService;
    private final SessionService sessionService;

    public ArticleController(ArticleService articleService, SessionService sessionService) {
        this.articleService = articleService;
        this.sessionService = sessionService;
    }

    @GetMapping("/articles/form")
    public String createForm(HttpSession session) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        return "qna/form";
    }

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest request,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        try {
            articleService.create(request, loggedInUserId);
            return REDIRECT_HOME;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return REDIRECT_JOIN;
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
                              HttpSession session) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        try {
            Article article = articleService.findArticle(id);
            model.addAttribute("article", article);
            return "qna/show";
        } catch (Exception e) {
            return REDIRECT_HOME;
        }
    }

    @GetMapping("/articles/{id}/form")
    public String updateForm(@PathVariable Long id,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        try {
            Article article = articleService.findArticle(id);

            if (!article.getWriter().getId().equals(loggedInUserId)) {
                model.addAttribute("errorMessage", "본인의 게시글만 수정할 수 있습니다.");
                return ERROR;
            }
            model.addAttribute("article", article);
            return "qna/updateForm";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return REDIRECT_HOME;
        }
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable Long id,
                                ArticleRequest request,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        try {

            Article article = articleService.findArticle(id);

            if (!article.getWriter().getId().equals(loggedInUserId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "본인의 게시글만 수정할 수 있습니다.");
                return "redirect:/article/" + id;
            }

            articleService.updateArticle(id, request);
            return "redirect:/articles/" + id;

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/" + id + "/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/" + id + "/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "질문 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/articles/" + id + "/form";
        }
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return REDIRECT_LOGIN;
        }

        try {
            Article article = articleService.findArticle(id);

            if (!article.getWriter().getId().equals(loggedInUserId)) {
                model.addAttribute("errorMessage", "본인의 게시글만 삭제할 수 있습니다.");
                return ERROR;
            }

            articleService.delete(id);
            return REDIRECT_HOME;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return REDIRECT_HOME;
        }
    }
}
