package codesquad.codestagram.domain;

import codesquad.codestagram.dto.BoardForm;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board", cascade=CascadeType.ALL, orphanRemoval = true)
    private final List<Reply> replyList = new ArrayList<>();

    private boolean deleted = false;

    protected Board() { //JPA는 엔티티 클래스를 reflection을 통해 인스턴스화 할때 기본 생성자(파라미터가 없는 생성자) 사용
    }

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Board form(BoardForm form) {
        return new Board(form.getTitle(), form.getContent());
    }


    public void updateFrom(BoardForm form) {
        this.title = form.getTitle();
        this.content = form.getContent();
    }

    //댓글 리스트에 추가
    public void addReply(Reply reply) {
        replyList.add(reply);
        reply.setBoard(this);
    }

    //댓글 리스트에서 삭제
    public void removeReply(Reply reply) {
        replyList.remove(reply);
        reply.setBoard(null); //실제 DB에서 해당 댓글이 없어지더라도, 엔티티간의 연관관계는 인메모리에서 관리되는 것이기 때문에, 관계를 명시적으로 끊어주기.
    }

    //댓글리스트 반환
    public List<Reply> getReplies() {
        return replyList;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

}
