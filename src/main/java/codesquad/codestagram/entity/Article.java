package codesquad.codestagram.entity;

public class Article {

    private int id;        // 게시글 id
    private String title;  // 게시글 제목
    private String content; // 게시글 내용

    // 생성자
    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
