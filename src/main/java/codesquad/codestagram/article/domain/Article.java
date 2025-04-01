package codesquad.codestagram.article.domain;

import codesquad.codestagram.user.domain.User;
import jakarta.persistence.*;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User writer;
    private String title;
    private String content;

    public Article(User writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public Article() {

    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void updateArticle(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

