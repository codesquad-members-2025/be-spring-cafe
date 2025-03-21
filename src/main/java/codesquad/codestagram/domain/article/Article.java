package codesquad.codestagram.domain.article;

import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String content;

    public Article(Long UserId, String title, String content) {
        this.userId = UserId;
        this.title = title;
        this.content = content;
    }

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSameWriter(Long userId) {
        return this.userId.equals(userId);
    }

}
