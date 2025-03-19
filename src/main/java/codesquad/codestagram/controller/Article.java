package codesquad.codestagram.controller;

public class Article {

    private static int count = 0;

    private int index;

    private String writer;

    private String title;

    private String contents;

    public Article(String writer, String title, String contents) {
        this.index = ++count;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public int getIndex() {
        return index;
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
