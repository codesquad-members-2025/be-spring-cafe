package codesquad.codestagram.reply.controller;

import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.reply.dto.ReplyRequest;
import codesquad.codestagram.reply.service.ReplyService;
import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static codesquad.codestagram.article.controller.ArticleController.ERROR_MESSAGE;
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
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        try {
            replyService.createReply(articleId, loggedInUserId, replyRequest.content());
            return "redirect:/articles/" + articleId;
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/" + articleId;
        }
    }

    @PutMapping("/{replyId}")
    public String updateReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              @ModelAttribute ReplyRequest replyRequest,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        try {
            replyService.updateReply(replyId, loggedInUserId, replyRequest.content());
            return "redirect:/articles/" + articleId;
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
            return "redirect:/articles/" + articleId;
        }
    }

    @DeleteMapping("/{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpServletRequest request) {

        HttpSession session = request.getSession();
        Optional<Long> loggedInUserIdOpt = sessionService.getLoggedInUserIdOpt(session);
        if (loggedInUserIdOpt.isEmpty()) {
            sessionService.saveRedirectUrl(session, request.getRequestURI());
            return REDIRECT_LOGIN;
        }

        Long loggedInUserId = loggedInUserIdOpt.get();
        replyService.deleteReply(replyId, loggedInUserId);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{replyId}")
    public String redirectToArticle(@PathVariable Long articleId,
                                    @PathVariable Long replyId) {
        // #reply-id: url 해시를 통해 해당 댓글로 스크롤
        return "redirect:/articles/" + articleId + "#reply-" + replyId;
    }
}
