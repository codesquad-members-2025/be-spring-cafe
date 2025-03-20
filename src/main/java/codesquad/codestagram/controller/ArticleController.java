package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @PostMapping
    public String writeArticle(@ModelAttribute Article article) {
        articleRepository.save(article);
        return "redirect:/";
    }
}
