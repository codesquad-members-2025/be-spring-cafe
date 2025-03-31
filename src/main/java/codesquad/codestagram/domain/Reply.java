package codesquad.codestagram.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Where(clause = "deleted = false")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String text;
    private LocalDateTime createdAt;
    private boolean deleted = false;

    protected Reply() {}

    public Reply(User user, Article article, String text) {
        this.article = article;
        this.user = user;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public void softDelete() {
        this.deleted = true;
    }

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }

    public boolean isAuthor(User user) {
        return this.user.equals(user);
    }
}

