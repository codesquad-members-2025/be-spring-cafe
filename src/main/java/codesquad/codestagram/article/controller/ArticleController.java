package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    //글쓰기 폼 렌더링
    @GetMapping("/qna/form")
    public String showArticleForm(Model model){
        model.addAttribute("article", new Article(null, "", "", ""));
        return "/qna/form";
    }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article article){
        articleService.write(article);
        return "redirect:/";
    }


}
