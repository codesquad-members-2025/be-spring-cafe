package codesquad.codestagram.domain.reply;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.reply.dto.ReplyResponseDto;
import codesquad.codestagram.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles/{articleId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@PathVariable Long articleId) {
        List<ReplyResponseDto> replies = replyService.findRepliesByArticle(articleId)
                .stream()
                .map(ReplyResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(replies);
    }

    @PostMapping("")
    public String addReply(@PathVariable Long articleId,
                           @RequestParam String content,
                           HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        replyService.addReply(articleId, user, content);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        replyService.deleteReply(replyId, user);

        return "redirect:/articles/" + articleId;
    }

}
