package codesquad.codestagram.article.domain;

import codesquad.codestagram.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name="articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    // 기본 생성자 추가 (Thymeleaf 바인딩을 위해 필요)
    public Article() {}

    //기본 생성자
    public Article (Long articleId, User writer, String title, String contents){
        this.articleId=articleId;
        this.writer=writer;
        this.title=title;
        this.contents = contents;
    }


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
