package codesquad.codestagram.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {

    private static final List<Article> articleList = new ArrayList<>();

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute Article article){
        articleList.add(article);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showArticles(Model model){
        model.addAttribute("articles", articleList);
        return "article/index";
    }


}
