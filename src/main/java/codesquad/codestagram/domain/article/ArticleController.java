package codesquad.codestagram.domain.article;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.reply.Reply;
import codesquad.codestagram.domain.reply.ReplyService;
import codesquad.codestagram.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 게시물 생성
    @PostMapping("")
    public String addArticle(@RequestParam String title,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        Article savedArticle = articleService.createArticle(title, content, user);

        return "redirect:/articles/" + savedArticle.getId();
    }

    // 게시물 상세 조회
    @GetMapping("{id}")
    public String viewArticle(@PathVariable Long id,
                              Model model) {
        Article article = articleService.findArticle(id);
        model.addAttribute("article", article);

        return "article/view";
    }

    // 게시물 작성 폼
    @GetMapping("write")
    public String writeArticle() {
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
        articleService.deleteArticle(id, user);

        return "redirect:/";
    }

}
