package codesquad.codestagram.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return createdAt.format(dateFormatter)+" "+createdAt.format(timeFormatter);
    }
}
