package codesquad.codestagram.domain.reply;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.reply.dto.ReplyRequestDto;
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
    public ResponseEntity<ReplyResponseDto> addReply(@PathVariable Long articleId,
                                                     @RequestBody ReplyRequestDto replyRequestDto,
                                                     HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        Reply newReply = replyService.addReply(articleId, user, replyRequestDto.content());

        return ResponseEntity.ok(ReplyResponseDto.of(newReply));
    }

    @DeleteMapping("{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId,
                                            HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        replyService.deleteReply(replyId, user);

        return ResponseEntity.ok().build();
    }

}
