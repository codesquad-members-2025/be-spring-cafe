package codesquad.codestagram.reply.controller;

import codesquad.codestagram.reply.dto.ReplyRequest;
import codesquad.codestagram.reply.service.ReplyService;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
                              @ModelAttribute ReplyRequest replyRequest,
                              HttpSession session,
                              HttpServletRequest request) {

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        replyService.createReply(articleId, loggedInUserId, replyRequest);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{replyId}")
    public String updateReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              @ModelAttribute ReplyRequest replyRequest,
                              HttpSession session,
                              HttpServletRequest request) {

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        replyService.updateReply(replyId, loggedInUserId, replyRequest);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpSession session,
                              HttpServletRequest request) {

        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        replyService.deleteReply(replyId, loggedInUserId);
        return "redirect:/articles/" + articleId;
    }
}
