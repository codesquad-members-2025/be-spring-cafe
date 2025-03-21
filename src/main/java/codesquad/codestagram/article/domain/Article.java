package codesquad.codestagram.article.domain;

public class Article {

    Long id;

    String writer;

    String title;

    String contents;

    //기본 생성자
    public Article(Long id, String writer, String title, String contents){
        this.id=id;
        this.writer=writer;
        this.title=title;
        this.contents = contents;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
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
