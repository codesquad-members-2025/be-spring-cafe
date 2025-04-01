package codesquad.codestagram.domain;

import codesquad.codestagram.dto.BoardForm;
import jakarta.persistence.*;

@Entity
public class Board {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;
    private String writer;
    //아직 로그인 기능 없음 -> writer만 저장

    protected Board() { //JPA는 엔티티 클래스를 reflection을 통해 인스턴스화 할때 기본 생성자(파라미터가 없는 생성자) 사용
    }

    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public static Board form(BoardForm form) {
        return new Board(form.getTitle(), form.getContent(), form.getWriter());
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
