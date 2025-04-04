package codesquad.codestagram.dto;

import codesquad.codestagram.controller.Article;
import codesquad.codestagram.controller.User;

public class RequestArticleDto {

    private String writer;

    private String title;

    private String contents;

    private Long userId;

    public RequestArticleDto(String writer, String title, String contents, Long userId) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getUserId() {
        return userId;
    }

    public Article toArticle(User user){
        return new Article(writer, title, contents, user);
    }

}
