package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.service.ArticleService;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
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
    public String showArticleForm(HttpSession session){
        if(SessionUtil.isUserLoggedIn(session)){
            return "qna/form";
        }
        else return "redirect:/auth/login";
    }

    //게시글 데이터 저장하기
    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article article, HttpSession session){
        if(SessionUtil.isUserLoggedIn(session)){
            User user = (User) session.getAttribute(SessionUtil.SESSION_USER_KEY);
            article.setWriter(user.getName());
            articleService.write(article);
            return "redirect:/";
        }
        //로그인하지 않은 사용자가 글쓰기 페이지에 접근할 경우 로그인 페이지로 이동
        else return "redirect:/auth/login";
    }

    //게시글 목록 구현하기 -> 로그인하지 않은 사용자는 게시글 목록만 볼 수 있다
    @GetMapping("/")
    public String getArticleList(Model model){
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }


    // 게시글 상세 보기 구현하기 -> 로그인한 사용자만 게시글의 세부 내용을 볼 수 있다
    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable("id") Long id, Model model, HttpSession session) {
        if(SessionUtil.isUserLoggedIn(session)){
            Article article = articleService.getArticleById(id);
            model.addAttribute("article", article);
            return "qna/show";
        }
        else {
            return "error/401";
        }

    }




}
