package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;

public class ArticleForm {
    private String title;
    private String content;

    protected ArticleForm() {}

    public ArticleForm(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article createParsedArticle(User user){
        return new Article(user, title, content.replace("\n", "<br>"));
    }
}
