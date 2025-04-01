package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyApiController {
    public static final String NEED_LOGIN = "로그인이 필요합니다.";
    private final ReplyService replyService;
    private final ArticleService articleService;
    private final AuthService authService;

    public ReplyApiController(ReplyService replyService, ArticleService articleService, AuthService authService) {
        this.replyService = replyService;
        this.articleService = articleService;
        this.authService = authService;
    }

    @PostMapping("/api/reply/{articleId}")
    public ResponseEntity<?> addReply(@RequestParam String content, @PathVariable Long articleId, HttpSession session) {
        if (authService.checkLogin(session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NEED_LOGIN);
        Article findArticle;
        User user = (User) session.getAttribute(SESSIONED_USER);

        try {
            findArticle = articleService.findArticleById(articleId);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        Reply savedReply = replyService.addReply(content, findArticle, user);

        return ResponseEntity.ok(savedReply);
    }
}
