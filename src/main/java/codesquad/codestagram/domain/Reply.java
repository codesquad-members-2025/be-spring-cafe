package codesquad.codestagram.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Reply {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reply() {}

    public Reply(String content, Article article, User user) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.article = article;
        this.user = user;
        isDeleted = false;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Article getArticle() {
        return article;
    }

    public User getUser() {
        return user;
    }

    public void changeDeleteStatus(boolean status){
        isDeleted = status;
    }
}
