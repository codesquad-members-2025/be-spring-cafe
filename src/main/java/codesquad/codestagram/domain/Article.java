package codesquad.codestagram.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Article {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected Article(){}

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
