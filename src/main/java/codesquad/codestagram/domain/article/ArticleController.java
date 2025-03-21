package codesquad.codestagram.domain.article;

import codesquad.codestagram.common.constants.SessionConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping("")
    public String addArticle(@RequestParam String title,
                             @RequestParam String content) {

        Article article = new Article(title, content);
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

}
