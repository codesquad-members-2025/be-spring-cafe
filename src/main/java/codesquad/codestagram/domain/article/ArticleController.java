package codesquad.codestagram.domain.article;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.ReplyService;
import codesquad.codestagram.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ReplyService replyService;

    public ArticleController(ArticleService articleService, ReplyService replyService) {
        this.articleService = articleService;
        this.replyService = replyService;
    }

    // 게시물 생성
    @PostMapping("")
    public String addArticle(@RequestParam String title,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        articleService.createArticle(title, content, user);

        return "redirect:/";
    }

    // 게시물 상세 조회
    @GetMapping("{id}")
    public String viewArticle(@PathVariable Long id,
                              Model model,
                              HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Article article = articleService.findArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("replies", replyService.findRepliesByArticle(id));

        return "article/view";
    }

    // 게시물 작성 폼
    @GetMapping("write")
    public String writeArticle(HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            // 로그인 폼으로 이동
            return "redirect:/auth/login";
        }

        return "article/form";
    }

    // 게시물 수정 폼
    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        Article article = articleService.getAuthorizedArticle(id, user);
        model.addAttribute("article", article);

        return "article/edit";
    }

    // 게시물 수정 요청
    @PutMapping("{id}")
    public String updateArticle(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String content,
                                HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        articleService.updateArticle(id, title, content, user);

        return "redirect:/articles/" + id;
    }

    // 게시물 삭제 요청
    @DeleteMapping("{id}")
    public String deleteArticle(@PathVariable Long id,
                                HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        articleService.deleteArticle(id, user);

        return "redirect:/";
    }

}
