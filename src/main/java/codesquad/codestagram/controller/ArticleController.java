package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articleList = articleService.findAllArticles();
        model.addAttribute("articleList", articleList);
        return "index";
    }

    @PostMapping("/qna/create")
    public String create(ArticleForm articleForm) {
        Optional<User> user = userService.findByUserId(articleForm.getUserId());
        if(user.isPresent()) {
            String contentWithBreaks = articleForm.getContent().replace("\n", "<br>");
            Article article = new Article(user.get(),articleForm.getTitle(),contentWithBreaks);
            articleService.saveArticle(article);
        }
        return "redirect:/";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable int index, Model model) {
        Optional<Article> article = articleService.findArticleById(index);
        if(article.isPresent()){
            model.addAttribute("article", article.get());
            return "qna/show";
        } else {
            return "redirect:/";
        }
    }
}
