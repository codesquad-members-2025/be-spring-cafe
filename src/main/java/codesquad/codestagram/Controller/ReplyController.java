package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyForm;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.util.AuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    //댓글작성(추가)
    @PostMapping("/boards/{boardId}/replies")
    public String addReply(@PathVariable Long boardId,
                           @ModelAttribute ReplyForm form,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        //todo 인터셉트로
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }
        User user = (User) session.getAttribute("user");
        replyService.addReply(form, user, boardId);
        return "redirect:/boards/" + boardId; //게시물 상세보기로
    }

    //댓글 삭제
    @DeleteMapping("/boards/{boardId}/replies/{replyId}")
    public String deleteReply(@PathVariable("boardId") Long boardId,
                              @PathVariable("replyId") Long replyId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }
        User user = (User) session.getAttribute("user");

        if (!replyService.deleteReply(replyId, user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 권한이 없습니다.");
        }
        return "redirect:/boards/" + boardId;
    }
}
