package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

    @PostMapping("/questions")
    public String ask(
            @RequestParam("writer") String writer,
            @RequestParam("title") String title,
            @RequestParam("contents") String contents) {
        Article article = new Article();
        article.setWriter(writer);
        article.setTitle(title);
        article.setContents(contents);

        
    }

}
