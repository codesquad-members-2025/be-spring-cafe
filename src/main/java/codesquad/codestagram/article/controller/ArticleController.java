package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showArticleForm(){
        return "/qna/form";
    }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article article){
        articleService.write(article);
        return "redirect:/";
    }

    @GetMapping("/")
    public String getArticleList(Model model){
        List<Article> articles = articleRepository.getAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }


    // 게시글 상세 페이지
    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable("id") int id, Model model) {
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        return "/qna/show";
    }
}
