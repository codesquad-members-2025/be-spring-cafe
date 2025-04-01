package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyDto;
import codesquad.codestagram.dto.ReplyDto.AddReplyResponseDto;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReplyApiController {
    public static final String NEED_LOGIN = "로그인이 필요합니다.";
    public static final String ONLY_AUTHOR_DELETE ="작성자만 댓글을 지울 수 있습니다.";
    private final ReplyService replyService;
    private final ArticleService articleService;
    private final AuthService authService;

    public ReplyApiController(ReplyService replyService, ArticleService articleService, AuthService authService) {
        this.replyService = replyService;
        this.articleService = articleService;
        this.authService = authService;
    }

    @PostMapping("/reply/{articleId}")
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
        return ResponseEntity.ok(AddReplyResponseDto.ReplyToDto(savedReply));
    }

    @DeleteMapping("/article/{articleId}/reply/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long articleId, @PathVariable Long replyId, HttpSession session){
        Map<String, Boolean> response = new HashMap<>();
        if (authService.checkLogin(session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NEED_LOGIN);
        User user = (User) session.getAttribute(SESSIONED_USER);
        Reply reply;

        try{
            reply = replyService.findReplyByIdAndNotDeleted(replyId);
        }catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        if (replyService.isNotReplyAuthor(user, reply)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ONLY_AUTHOR_DELETE);
        }
        replyService.deleteReply(reply);
        response.put("isSuccess", true);
        return ResponseEntity.ok(response);
    }
}
