package codesquad.codestagram.domain.article;

import codesquad.codestagram.common.constants.SessionConstants;
import codesquad.codestagram.domain.auth.UnauthorizedException;
import codesquad.codestagram.domain.user.User;
import codesquad.codestagram.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public String addArticle(@RequestParam String title,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            return "redirect:/auth/login";
        }

        Article article = new Article(user.getId(), title, content);
        articleRepository.save(article);

        return "redirect:/";
    }

    @GetMapping("{id}")
    public String viewArticle(@PathVariable Long id, Model model) {
        Optional<Article> article = articleRepository.findById(id);

        if (article.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("article", article.get());

        return "article/view";
    }

    @GetMapping("write")
    public String writeArticle(HttpSession session, Model model) {
        if (session.getAttribute(SessionConstants.USER_SESSION_KEY) == null) {
            return "redirect:/auth/login";
        }

        return "article/form";
    }

    @GetMapping("{id}/form")
    public String showUpdateForm(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
        Optional<Article> article = articleRepository.findById(id);
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);

        if (article.isEmpty()) {
            return "redirect:/";
        }

        if (!article.get().isSameWriter(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        model.addAttribute("article", article.get());

        return "article/edit";
    }

    @PutMapping("{id}")
    @Transactional
    public String updateArticle(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String content,
                                HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            return "redirect:/";
        }

        Article articleEntity = article.get();
        if (!articleEntity.isSameWriter(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        articleEntity.setContent(content);
        articleEntity.setTitle(title);

        return "redirect:/articles/" + id;
    }

    @DeleteMapping("{id}")
    @Transactional
    public String deleteArticle(@PathVariable Long id,
                                HttpSession session) {
        User user = (User) session.getAttribute(SessionConstants.USER_SESSION_KEY);
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            return "redirect:/";
        }

        Article articleEntity = article.get();
        if (!articleEntity.isSameWriter(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        articleRepository.delete(articleEntity);

        return "redirect:/";
    }

}
