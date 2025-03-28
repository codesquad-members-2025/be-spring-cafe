package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.dto.ReplyViewDto;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.utility.TextUtility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final ReplyService replyService;

    public ArticleController(ArticleService articleService, ReplyService replyService) {
        this.articleService = articleService;
        this.replyService = replyService;
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
    public String viewArticle(@PathVariable Long index, Model model, HttpSession session) {
        Article article = articleService.findArticleById(index);
        List<ReplyViewDto> replyViewDtoList = replyService.findRepliesByArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        model.addAttribute("replyDtoList", replyViewDtoList);
        model.addAttribute("replyCount", replyViewDtoList.size());
        return "qna/show";
    }

    @DeleteMapping("/articles/{index}")
    public String delete(@PathVariable Long index, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Article article = articleService.findArticleIfOwner(loginUser, index);
        articleService.delete(article);
        return "redirect:/";
    }

    @GetMapping("/articles/{index}/update-form")
    public String updateForm(@PathVariable Long index, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Article article = articleService.findArticleIfOwner(loginUser, index);

        model.addAttribute("article", article);
        model.addAttribute("parsedContent", TextUtility.escapeAndConvertNewlines(article.getContent()));
        return "qna/update-form";
    }

    @PutMapping("/articles/{index}/update")
    public String update(@PathVariable Long index, ArticleForm articleForm, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        articleService.update(loginUser, index, articleForm);

        return "redirect:/articles/" + index;
    }
}
