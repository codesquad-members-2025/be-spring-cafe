package codesquad.codestagram.article.domain;

import codesquad.codestagram.reply.domain.Reply;
import codesquad.codestagram.user.domain.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Article(User writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdAt = this.updatedAt =  LocalDateTime.now();
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
        this.updatedAt = LocalDateTime.now();
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public Long getReplyCount() {
        return replies.stream()
                .filter(reply -> !reply.isDeleted())
                .count();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    public boolean hasRepliesByOtherUsers() {
        Long articleWriterId = this.writer.getId();
        return replies.stream()
                .filter(reply -> !reply.isDeleted())
                .anyMatch(reply -> !reply.isWrittenBy(articleWriterId));
    }

    public void markRepliesAsDeleted() {
        replies.forEach(Reply::markAsDeleted);
    }
}

