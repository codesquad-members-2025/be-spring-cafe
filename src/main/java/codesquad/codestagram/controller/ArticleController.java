package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private List<Article> articles = Collections.synchronizedList(new ArrayList<>());

    @PostMapping
    public String writeArticle(@ModelAttribute Article article) {
        articles.add(article);
        return "redirect:/";
    }
}
