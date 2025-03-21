package codesquad.codestagram.article.domain;

public class Article {
    private Long articleId;
    private String writer;
    private String title;
    private String content;

    public Article(String writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}

