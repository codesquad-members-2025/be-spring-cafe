package codesquad.codestagram.domain;

import codesquad.codestagram.dto.ArticleForm;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Where(clause = "deleted = false")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // ✅ 외래 키 설정
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @Where(clause = "deleted = false")
    private List<Reply> replies = new ArrayList<>();
    private LocalDateTime createdAt;
    private boolean deleted = false;

    protected Article() {}

    public Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public List<Reply> getReplies() {
        return replies;
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

    public Reply addReply(User user, String text) {
        Reply reply = new Reply(user, this, text);
        replies.add(reply);
        return reply;
    }

    public void softDelete() {
        deleted = true;
        replies.forEach(Reply::softDelete);
    }

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
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
