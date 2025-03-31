package codesquad.codestagram.controller;


import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute RequestArticleDto requestArticle) {
        articleService.save(requestArticle);
        return "redirect:/";
    }

    @GetMapping("/articles")
    public String getArticleForm(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "user/login";
        }
        return "qna/form.html";
    }

    @GetMapping("/")
    public String showArticles(Model model){

        List<Article> articleList = articleService.findAll();
        model.addAttribute("articles", articleList);
        return "article/index";
    }


    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article/show";
    }


}
