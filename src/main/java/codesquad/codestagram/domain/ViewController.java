package codesquad.codestagram.domain;

import codesquad.codestagram.common.dto.PaginationDto;
import codesquad.codestagram.domain.article.Article;
import codesquad.codestagram.domain.article.ArticleRepository;
import codesquad.codestagram.domain.article.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewController {

    private final ArticleService articleService;

    public ViewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model) {
        Page<Article> articles = articleService.findArticles(page);
        PaginationDto pagination = PaginationDto.of(articles.getNumber() + 1,
                articles.getSize(),
                articles.getTotalElements());

        model.addAttribute("articles", articles);
        model.addAttribute("pagination", pagination);

        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

}
