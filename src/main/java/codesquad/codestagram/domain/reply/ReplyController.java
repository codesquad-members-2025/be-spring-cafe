package codesquad.codestagram.domain.reply;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.reply.dto.ReplyRequestDto;
import codesquad.codestagram.domain.reply.dto.ReplyResponseDto;
import codesquad.codestagram.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static codesquad.codestagram.common.constants.PaginationConstatns.DEFAULT_SORT_PROPERTY;
import static codesquad.codestagram.common.constants.PaginationConstatns.REPLY_PAGE_SIZE;

@RestController
@RequestMapping("/articles/{articleId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ReplyResponseDto>> getReplies(@PathVariable Long articleId,
                                                             @PageableDefault(size = REPLY_PAGE_SIZE, sort = DEFAULT_SORT_PROPERTY, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Reply> repliesPage = replyService.findRepliesByArticle(articleId, pageable);
        Page<ReplyResponseDto> replies = repliesPage.map(ReplyResponseDto::of);

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
