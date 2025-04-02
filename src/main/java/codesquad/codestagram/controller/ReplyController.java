package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyResponseDto;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/articles/{index}/answers")
    public ResponseEntity<ReplyResponseDto> addAnswer(@PathVariable Long index, @RequestParam String text, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Reply reply = replyService.addReplyToArticle(user, index, text);
        return ResponseEntity.ok(ReplyResponseDto.from(reply));
    }

    @DeleteMapping("/articles/{articleId}/answers/{replyId}")
    public String addAnswer(@PathVariable Long articleId, @PathVariable Long replyId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Reply reply = replyService.findReplyIfOwner(user,replyId);
        replyService.deleteReply(reply);
        return "redirect:/articles/" + articleId;
    }
}
