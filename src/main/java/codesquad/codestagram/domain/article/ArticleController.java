package codesquad.codestagram.domain.article;

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

        long id = articleRepository.count() + 1;
        Article article = new Article(id, title, content);
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

}
