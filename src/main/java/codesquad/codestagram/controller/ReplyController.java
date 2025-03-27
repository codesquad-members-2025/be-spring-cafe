package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.ArticleController.checkLogin;
import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.service.UserService;
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
    private final UserService userService;

    public ReplyController(ReplyService replyService, ArticleService articleService, UserService userService) {
        this.replyService = replyService;
        this.articleService = articleService;
        this.userService = userService;
    }

    @PostMapping("/reply/{articleId}")
    public String addReply(@RequestParam String content, @PathVariable Long articleId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (checkLogin(session)) return "redirect:/login";
        Article findArticle;
        User user = (User) session.getAttribute(SESSIONED_USER);

        try {
            findArticle = articleService.findArticleById(articleId);
        }catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/";
        }
        replyService.addReply(content, findArticle, user);

        return "redirect:/articles/" + articleId;
    }
}
