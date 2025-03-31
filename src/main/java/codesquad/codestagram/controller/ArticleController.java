package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        User loginUser = (User) session.getAttribute(SESSION_LOGIN_USER);

        if (session.getAttribute(SESSION_LOGIN_USER) == null) {
            return "redirect:/users/loginForm";
        }

        article.setUser(loginUser);

        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String viewArticle(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (session.getAttribute(SESSION_LOGIN_USER) == null) {
            return "redirect:/users/loginForm";
        }

        Article article = articleRepository.findById(id).orElseThrow();
        model.addAttribute("article", article);
        return "article/detail";
    }

    @GetMapping("/article/{id}/form")
    public String viewUpdateForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SESSION_LOGIN_USER);

        if (loginUser == null) { //로그인 상태인지 확인
            return "redirect:/users/loginForm";
        }

        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (loginUser.getId() != article.getUser().getId()) {
            redirectAttributes.addFlashAttribute("alertMessage", "작성자 ID와 사용자 ID가 일치하지 않습니다.");
            return "redirect:/";
        }

        model.addAttribute("article", article);

        return "article/updateForm";
    }

    @PutMapping("/article/{id}/update")
    public String updateArticle(@PathVariable("id") Long id, @ModelAttribute Article article, HttpSession session
        , RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SESSION_LOGIN_USER);

        if (loginUser == null) { //로그인 상태인지 확인
            return "redirect:/users/loginForm";
        }

        Article findArticle = articleRepository.findById(id).orElseThrow();

        if (loginUser.getId() != findArticle.getUser().getId()) {
            redirectAttributes.addFlashAttribute("alertMessage", "작성자 ID와 사용자 ID가 일치하지 않습니다.");
            return "redirect:/";
        }

        article.setUser(loginUser);
        articleRepository.save(article);
        return "redirect:/article/" + id;
    }

    @DeleteMapping("/article/{id}/delete")
    public String deleteArticle(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SESSION_LOGIN_USER);

        if (loginUser == null) { //로그인 상태인지 확인
            return "redirect:/users/loginForm";
        }

        Article findArticle = articleRepository.findById(id).orElseThrow();

        if (loginUser.getId() != findArticle.getUser().getId()) {
            redirectAttributes.addFlashAttribute("alertMessage", "작성자 ID와 사용자 ID가 일치하지 않습니다.");
            return "redirect:/article/" + id;
        }

        articleRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("alertMessage", "게시글이 정상적으로 삭제되었습니다.");
        return "redirect:/";
    }
}