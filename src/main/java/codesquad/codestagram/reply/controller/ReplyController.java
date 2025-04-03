package codesquad.codestagram.reply.controller;

import codesquad.codestagram.reply.dto.ReplyRequest;
import codesquad.codestagram.reply.service.ReplyService;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static codesquad.codestagram.article.controller.ArticleController.REDIRECT_LOGIN;

@Controller
@RequestMapping("/articles/{articleId}/replies")
public class ReplyController {
    private final SessionService sessionService;
    private final ReplyService replyService;

    public ReplyController(SessionService sessionService, ReplyService replyService) {
        this.sessionService = sessionService;
        this.replyService = replyService;
    }

    @PostMapping
    public String createReply(@PathVariable Long articleId,
                              @ModelAttribute ReplyRequest request,
                              HttpSession session) {
        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    replyService.createReply(articleId, loggedInUserId, request);
                    return "redirect:/articles/" + articleId;
                }).orElse(REDIRECT_LOGIN);
    }

    @PutMapping("/{replyId}")
    public String updateReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              @ModelAttribute ReplyRequest request,
                              HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    replyService.updateReply(replyId, loggedInUserId, request);
                    return "redirect:/articles/" + articleId;
                }).orElse(REDIRECT_LOGIN);
    }

    @DeleteMapping("/{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpSession session) {

        return sessionService.getLoggedInUserId(session)
                .map(loggedInUserId -> {
                    replyService.deleteReply(replyId, loggedInUserId);
                    return "redirect:/articles/" + articleId;
                }).orElse(REDIRECT_LOGIN);
    }
}
