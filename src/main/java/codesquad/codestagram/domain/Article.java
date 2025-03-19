package codesquad.codestagram.domain;

public class Article {
    private String title;
    private String content;
    private User user;

    public Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
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
}
