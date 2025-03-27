package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.ArticleController.checkLogin;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReplyController {
    private final ReplyService replyService;
    private final ArticleService articleService;

    public ReplyController(ReplyService replyService, ArticleService articleService) {
        this.replyService = replyService;
        this.articleService = articleService;
    }

    @PostMapping("/reply/{articleId}")
    public String addReply(@RequestParam String content, @PathVariable Long articleId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";
        Article findArticle;
        try {
            findArticle = articleService.findArticleById(articleId);
        }catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/";
        }
        replyService.addReply(content, findArticle);

        return "redirect:/articles/" + articleId;
    }

}
