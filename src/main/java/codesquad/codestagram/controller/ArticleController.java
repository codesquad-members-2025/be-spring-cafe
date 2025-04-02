package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static codesquad.codestagram.config.AppConstants.*;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String getArticles(Model model) {
        List<Article> articles = articleService.findArticleList();
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("/article")
    public String getArticleForm(HttpSession session) {
        return "article/write";
    }

    @PostMapping("/article")
    public String createArticle(@ModelAttribute Article article, HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        article.setUser(loginUser);
        articleService.saveArticle(article);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            Article article = articleService.findArticle(id);
            model.addAttribute("article", article);
            return "article/detail";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(ALERT_MESSAGE, e.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/article/{id}/form")
    public String getArticleUpdateForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);

        try {
            Article article = articleService.findArticle(id);

            if (loginUser.getId() != article.getUser().getId()) {
                redirectAttributes.addFlashAttribute(ALERT_MESSAGE, "작성자 ID와 사용자 ID가 일치하지 않습니다.");
                return "redirect:/article/" + id;
            }

            model.addAttribute("article", article);
            return "article/updateForm";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(ALERT_MESSAGE, e.getMessage());
            return "redirect:/";
        }



    }

    @PutMapping("/article/{id}/update")
    public String updateArticle(@PathVariable("id") Long id, @ModelAttribute Article article, HttpSession session
        , RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);

        boolean result = articleService.updateArticle(id, article, loginUser);

        if (!result) {
            redirectAttributes.addFlashAttribute(ALERT_MESSAGE, "작성자 ID와 사용자 ID가 일치하지 않습니다.");
            return "redirect:/article/" + id;
        }

        return "redirect:/article/" + id;
    }

    @DeleteMapping("/article/{id}/delete")
    public String deleteArticle(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);

        boolean result = articleService.removeArticle(id, loginUser);

        if (!result) {
            redirectAttributes.addFlashAttribute(ALERT_MESSAGE, "작성자 ID와 사용자 ID가 일치하지 않습니다.");
            return "redirect:/article/" + id;
        }

        redirectAttributes.addFlashAttribute(ALERT_MESSAGE, "게시글이 정상적으로 삭제되었습니다.");
        return "redirect:/";
    }
}
