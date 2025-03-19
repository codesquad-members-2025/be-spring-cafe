package codesquad.codestagram.domain;

public class Article {
    private String title;
    private String content;
    private User user;
    private int id;

    public Article(User user, String title, String content, int id) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.id = id;
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
}
