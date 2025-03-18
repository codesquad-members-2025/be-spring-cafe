package codesquad.codestagram.domain.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final List<Article> articles;

    public ArticleController() {
        this.articles = new ArrayList<>();
    }

    @PostMapping("")
    public String addArticle(@RequestParam String title,
                             @RequestParam String content) {
        int id = articles.size() + 1;
        Article article = new Article(id, title, content);
        articles.add(article);

        return "redirect:/";
    }

}
