package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ArticleController {

    private static final String SESSION_LOGIN_USER = "loginUser";

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String viewMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("/article")
    public String viewArticleForm(HttpSession session) {
        if (session.getAttribute(SESSION_LOGIN_USER) == null) {
            return "redirect:/users/loginForm";
        }
        return "article/write";
    }

    @PostMapping("/article")
    public String writeArticle(@ModelAttribute Article article, HttpSession session) {
        if (session.getAttribute(SESSION_LOGIN_USER) == null) {
            return "redirect:/users/loginForm";
        }
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String viewArticle(@PathVariable("id") Long index, Model model, HttpSession session) {
        if (session.getAttribute(SESSION_LOGIN_USER) == null) {
            return "redirect:/users/loginForm";
        }

        Article article = articleRepository.findById(index).orElseThrow();
        model.addAttribute("article", article);
        return "article/detail";
    }
}
