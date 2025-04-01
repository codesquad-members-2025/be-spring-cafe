package codesquad.codestagram.controller;



import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String getArticleForm(HttpSession session){
        if(session == null){
            return "user/login";
        }
        return "qna/form.html";
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute RequestArticleDto requestArticle) {
        articleService.save(requestArticle);
        return "redirect:/";
    }

    @GetMapping("/articles/edit/{userId}/{articleId}")
    public String showArticleEditForm(@PathVariable Long userId,
                                      @PathVariable Long articleId,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser.getId().equals(userId)){
            model.addAttribute("article", articleService.findById(articleId));
            return "article/edit";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 수정할 수 있습니다.");
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@ModelAttribute RequestArticleDto editArticleInfo,
                              @PathVariable Long articleId) {
        articleService.edit(articleId, editArticleInfo);
        return "redirect:/";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){
        Article article = articleService.findById(articleId);
        User loginUser = (User) session.getAttribute("loginUser");

        if(article.getUser().getId().equals(loginUser.getId())){
            articleService.delete(article);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
        return "redirect:/articles/" + articleId;
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
