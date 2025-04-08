package codesquad.codestagram.domain;

import codesquad.codestagram.domain.article.Article;
import codesquad.codestagram.domain.article.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static codesquad.codestagram.common.constants.PaginationConstants.*;

@Controller
public class ViewController {

    private final ArticleService articleService;

    public ViewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public String index(@PageableDefault(size = ARTICLE_PAGE_SIZE, sort = DEFAULT_SORT_PROPERTY, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        Page<Article> articles = articleService.findArticles(pageable);
        model.addAttribute("articles", articles);

        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

}
