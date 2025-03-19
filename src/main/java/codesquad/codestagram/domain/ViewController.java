package codesquad.codestagram.domain;

import codesquad.codestagram.domain.article.Article;
import codesquad.codestagram.domain.article.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    private final ArticleRepository articleRepository;

    public ViewController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);

        return "index";
    }

}
