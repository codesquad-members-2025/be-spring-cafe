package codesquad.codestagram.domain.reply;

import codesquad.codestagram.common.entity.BaseEntity;
import codesquad.codestagram.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "reply")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long articleId;
    private String content;
    private boolean deleted;

    public Reply(User user, Long articleId, String content) {
        this.user = user;
        this.articleId = articleId;
        this.content = content;
        this.deleted = false;
    }

    public Reply() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
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
        return this.user.getId().equals(userId);
    }

    public boolean isSameArticle(Long articleId) {
        return this.articleId.equals(articleId);
    }

}
