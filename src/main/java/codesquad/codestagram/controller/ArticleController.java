package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ArticleController {
    public static final String ARTICLES = "articles";
    public static final String AUTHOR = "author";

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute ArticleDto.ArticleRequestDto requestDto, Model model) {
        try {
            articleService.saveArticle(requestDto);
        }catch (IllegalArgumentException e){
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            return "articles/form";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleService.findArticles();
        model.addAttribute(ARTICLES, articles);
        return "home";
    }

    @GetMapping("articles/{articleId}")
    public String showArticleDetail(@PathVariable Long articleId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";
        Article article;
        try {
            article = articleService.findArticleById(articleId);
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/";
        }

        boolean isArticleAuthor = articleService.isArticleAuthor(session, article);
        model.addAttribute(AUTHOR, isArticleAuthor);
        model.addAttribute(article);

        return "articles/show";
    }

    @GetMapping("articles/{articleId}/edit")
    public String editArticleForm(@PathVariable Long articleId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";
        Article article;
        try {
            article = articleService.findArticleById(articleId);

            articleService.matchArticleAuthor(session, article);
        }catch (IllegalArgumentException | AccessDeniedException e){
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/" + articleId;
        }

        model.addAttribute(AUTHOR, true);
        model.addAttribute(article);

        return "articles/edit";
    }

    @PutMapping("articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, @RequestParam String title, @RequestParam String content,
                                HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";

        Article article;
        try {
            article = articleService.findArticleById(articleId);

            articleService.matchArticleAuthor(session, article);
        }catch (IllegalArgumentException | AccessDeniedException e){
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/" + articleId;
        }

        model.addAttribute(AUTHOR, true);
        articleService.updateArticle(articleId, title, content);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";

        Article article;
        try {
            article = articleService.findArticleById(articleId);
            articleService.matchArticleAuthor(session, article);
            articleService.delete(articleId);
        }catch (IllegalArgumentException | AccessDeniedException  e){
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/";
        }
        return "redirect:/";
    }

    public static boolean checkLogin(HttpSession session) {
        User sessionedUser = (User) session.getAttribute(SESSIONED_USER);
        return sessionedUser == null;
    }
}
