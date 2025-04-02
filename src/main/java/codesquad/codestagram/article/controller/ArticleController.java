package codesquad.codestagram.article.controller;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.service.ArticleService;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.login.service.LoginService;
import codesquad.codestagram.service.UserService;
import codesquad.codestagram.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static codesquad.codestagram.util.SessionUtil.SESSION_USER_KEY;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final LoginService loginService;

    public ArticleController(ArticleService articleService,LoginService loginService) {
        this.articleService = articleService;
        this.loginService = loginService;
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
            User user = (User) session.getAttribute(SESSION_USER_KEY);
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


    @GetMapping("/articles/{id}/updateForm")
    public String updateForm(@PathVariable("id") Long id, Model model, HttpSession session){
        if(SessionUtil.isUserLoggedIn(session)) {
            Article article = articleService.getArticleById(id);
            model.addAttribute("article", article);
            return "qna/updateForm";
        }else
            return "error/405";
    }



    @PutMapping("/articles/{id}/update")
    public String updateArticleById(
            @PathVariable("id")Long id,
            @ModelAttribute Article article,
            HttpSession session
    ){
        User loginUser = (User) session.getAttribute(SESSION_USER_KEY); //getAttribute가 Object 객체를 반환하기 때문에 User로 캐스팅 해줘야함

        if(loginUser == null){
            return "redirect:/auth/login";
        }

        Article existingArticle = articleService.getArticleById(id);// 기존 글 정보 가져오기

        if(existingArticle == null){
            return "redirect:/error/not-found"; // 글이 존재하지 않는 경우 404 페이지로 이동
        }
        boolean updatePossible = loginService.validateUserOwnership(loginUser, existingArticle.getWriter());

        if(updatePossible){
            articleService.updateArticle(id, article);
            return "redirect:/";
        }else{
            return "redirect:/error/forbidden"; //권한이 없을 경우 권한없음 페이지 띄우기
        }
    }



}
