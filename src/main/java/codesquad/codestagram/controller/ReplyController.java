package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/articles/{index}/answers")
    public String addAnswer(@PathVariable Long index, @RequestParam String text, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        replyService.addReply(user, index, text);
        return "redirect:/articles/{index}";
    }

    @DeleteMapping("/articles/{articleId}/answers/{replyId}")
    public String addAnswer(@PathVariable Long articleId, @PathVariable Long replyId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        replyService.deleteReply(user, replyId);
        return "redirect:/articles/" + articleId;
    }
}
