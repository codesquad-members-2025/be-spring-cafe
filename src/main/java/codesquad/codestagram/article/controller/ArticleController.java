package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
    }

    //글쓰기 폼 렌더링
    @GetMapping("/qna/form")
    public String showArticleForm(){
        return "/qna/form";
    }

    //게시글 데이터 저장하기
    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article article){
        articleService.write(article);
        return "redirect:/";
    }

    //게시글 목록 구현하기
    @GetMapping("/")
    public String getArticleList(Model model){
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }


    // 게시글 상세 보기 구현하기
    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "/qna/show";
    }

}
