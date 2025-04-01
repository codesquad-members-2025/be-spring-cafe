package codesquad.codestagram.domain;

import codesquad.codestagram.dto.BoardForm;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
public class Board {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;
    private String writer;
    //아직 로그인 기능 없음 -> writer만 저장
    public Board() {

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
