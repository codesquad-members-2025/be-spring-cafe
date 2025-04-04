package codesquad.codestagram.article.domain;

import codesquad.codestagram.user.domain.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Column(nullable = false)
    private User writer;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Article(User writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    protected Article() {

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateArticle(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

