package codesquad.codestagram.domain.reply;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles/{articleId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("")
    public String addReply(@PathVariable Long articleId,
                           @RequestParam String content,
                           HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        replyService.addReply(articleId, user, content);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("{replyId}")
    public String deleteReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        replyService.deleteReply(replyId, user);

        return "redirect:/articles/" + articleId;
    }

}
