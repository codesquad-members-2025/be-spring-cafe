package codesquad.codestagram.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_seq", referencedColumnName = "userSeq", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.uploadDate = LocalDateTime.now();
    }

    public Board() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getUser() { return user; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public String getWriter() {
        return user.getName();
    }
}
