package codesquad.codestagram.domain;

import codesquad.codestagram.dto.ArticleForm;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // ✅ 외래 키 설정
    private User user;
    private LocalDateTime createdAt;

    public Article() {}

    public Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public String getCreatedAt() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return createdAt.format(dateFormatter)+" "+createdAt.format(timeFormatter);
    }

    public boolean isAuthor(User user) {
        return this.user.equals(user);
    }

    public void update(ArticleForm articleForm) {
        title = articleForm.getTitle();
        content = articleForm.getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // 또는 Objects.hash(id)
    }
}
