package codesquad.codestagram.reply.domain;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Reply(Article article, User writer, String content) {
        this.article = article;
        this.writer = writer;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Reply() {
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public User getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isWrittenBy(Long userId) {
        return this.writer.getId().equals(userId);
    }
}
