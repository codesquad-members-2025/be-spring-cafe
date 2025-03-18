package codesquad.codestagram.domain.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        int id = articleRepository.size() + 1;
        Article article = new Article(id, title, content);
        articleRepository.save(article);

        return "redirect:/";
    }

}
