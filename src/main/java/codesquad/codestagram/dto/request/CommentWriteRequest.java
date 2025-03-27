package codesquad.codestagram.dto.request;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;

public class CommentWriteRequest {

    private String comment;
    private User user;
    private Article article;

    public CommentWriteRequest(String comment, User user, Article article) {
        this.comment = comment;
        this.user = user;
        this.article = article;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
