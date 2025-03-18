// ArticleController.java
package codesquad.codestagram.controller;

import codesquad.codestagram.entity.Article;
import codesquad.codestagram.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/qna")
public class ArticleController {

    @Autowired  //
    private ArticleService service;

    @GetMapping()
    public String viewQuestions(Model model) {
        List<Article> articles = service.getArticles();
        model.addAttribute("articles", articles);
        return "qna/show";
    }

    @GetMapping("/articles/{index}")
    public String viewArticle(@PathVariable("index") int index, Model model) {
        List<Article> articles = service.getArticles();
        if (index > 0 && index <= articles.size()) {
            Article article = articles.get(index - 1);
            model.addAttribute("article", article);
            return "qna/show";
        }
        return "redirect:/";
    }

    @GetMapping("/form")
    public String showForm() {
        return "qna/form";
    }

    @PostMapping("/write")
    public String submitArticle(@RequestParam("title") String title,@RequestParam("content") String content) {
        Article article = new Article(service.getArticles().size() + 1, title, content);
        service.addArticle(article);
        return "redirect:/qna";
    }
}
