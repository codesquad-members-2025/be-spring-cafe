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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final SessionService sessionService;

    public ArticleController(ArticleService articleService, SessionService sessionService) {
        this.articleService = articleService;
        this.sessionService = sessionService;
    }

    @GetMapping("/articles/form")
    public String createForm(HttpSession session) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        return "qna/form";
    }
    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest request,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        if (!loggedInUser.getUserId().equals(request.writerId())) {
            return "error";
        }

        try {
            articleService.create(request);
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/join";
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

        User loggedInUser = sessionService.getLoggedInUser(session);
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        try {
            Article article = articleService.findArticle(id);
            model.addAttribute("article", article);
            return "qna/show";
        } catch (Exception e) {
            return "redirect:/";
        }

    }
}
