package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.UserController.SESSIONED_USER;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
    public static final String ARTICLES = "articles";
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute ArticleDto.ArticleRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId()).get();
        Article article = requestDto.toArticle(user);
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute(ARTICLES, articles);
        return "home";
    }

    @GetMapping("articles/{articleId}")
    public String showArticleDetail(@PathVariable Long articleId, Model model, HttpSession session) {
        User sessionedUser = (User) session.getAttribute(SESSIONED_USER);
        // 로그인 체크
        if (sessionedUser == null) {
            return "redirect:/login";
        }

        Article article = articleRepository.findById(articleId).get();
        if (sessionedUser.getUserId().equals(article.getUser().getUserId())){
            model.addAttribute("author", true);
        }

        model.addAttribute(article);
        return "articles/show";
    }
}
