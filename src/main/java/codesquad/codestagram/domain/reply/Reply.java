package codesquad.codestagram.domain.reply;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reply")
public class Reply {

    @Id
    private Long id;

    private Long userId;
    private Long articleId;
    private String content;
    private boolean deleted;

    public Reply(Long userId, Long articleId, String content) {
        this.userId = userId;
        this.articleId = articleId;
        this.content = content;
        this.deleted = false;
    }

    public Reply() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isSameWriter(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean isSameArticle(Long articleId) {
        return this.articleId.equals(articleId);
    }

}
