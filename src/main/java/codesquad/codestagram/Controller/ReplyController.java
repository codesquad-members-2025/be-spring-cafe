package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyForm;
import codesquad.codestagram.exception.ReplyNotFoundException;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.util.AuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    //댓글작성(추가)
    @PostMapping("/boards/{boardId}/replies")
    public String addReply(@PathVariable("boardId") Long boardId,
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

    //댓글 수정
    @GetMapping("/boards/{boardId}/replies/{replyId}/edit")
    public String editReply(@PathVariable("boardId") Long boardId,
                            @PathVariable("replyId") Long replyId,
                            Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Reply reply = replyService.getReplyById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
        //로그인 여부 & 동일 사용자인지 확인
        if (!AuthUtil.isAuthorized(session, reply.getWriter().getId(), redirectAttributes)) {
            return "redirect:/boards/" + boardId;
        }
        model.addAttribute("reply", reply);
        return "redirect:/boards/" + boardId;
    }

    @PutMapping("/boards/{boardId}/replies/{replyId}/edit")
    public String updateReply(@PathVariable("boardId") Long boardId,
                              @PathVariable("replyId") Long replyId,
                              @ModelAttribute ReplyForm form,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Reply reply = replyService.getReplyById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
        //로그인 여부 & 동일 사용자인지 확인
        if (!AuthUtil.isAuthorized(session, reply.getWriter().getId(), redirectAttributes)) {
            return "redirect:/boards/" + boardId;
        }

        if (!replyService.updateReply(replyId, form, (User) session.getAttribute("user"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 수정에 실패했습니다.");
        }
        return "redirect:/boards/" + boardId;
    }

    //댓글 삭제
    @DeleteMapping("/boards/{boardId}/replies/{replyId}")
    public String deleteReply(@PathVariable("boardId") Long boardId,
                              @PathVariable("replyId") Long replyId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // 로그인 여부 체크
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }
        User user = (User) session.getAttribute("user");

        // 서비스 계층으로 삭제 요청 (예외 발생 시 글로벌 핸들러가 처리)
        replyService.deleteReply(replyId, user);
        return "redirect:/boards/" + boardId;
    }
}
