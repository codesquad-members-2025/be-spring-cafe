package codesquad.codestagram.reply.controller;

import codesquad.codestagram.user.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

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
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return "redirect:/users/login";
        }

        try {
            replyService.createReply(articleId, loggedInUserId, request);
            return "redirect:/articles/" + articleId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/" + articleId;
        }
    }

    @PutMapping("/{replyId}")
    public String updateReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              @ModelAttribute ReplyRequest request,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Long loggedInUserId = sessionService.getLoggedInUserId(session);
        if (loggedInUserId == null) {
            return "redirect:/users/login";
        }

        try {
            replyService.updateReply(replyId, loggedInUserId, request);
            return "redirect:/articles/" + articleId;
        } catch (NoSuchElementException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/" + articleId;
        }
    }

    @DeleteMapping("/{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Long loggedInUserId = sessionService.getLoggedInUserId(session);

        if (loggedInUserId == null) {
            return "redirect:/users/login";
        }

        try {
            replyService.deleteReply(replyId, loggedInUserId);
            return "redirect:/articles/" + articleId;
        } catch (IllegalArgumentException | NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/" + articleId;
        }
    }
}
