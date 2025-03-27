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
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Reply() {}

    public Reply(String content, Article article) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.article = article;
    }
}
