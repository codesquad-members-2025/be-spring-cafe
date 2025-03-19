package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.Board;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("boardMapRepository")
public class BoardMapRepository {
    private Map<Long, Board> boardMap = new HashMap<>();
    private Long boardSeq = 1L;

    public Board save(Board board){
        board = new Board(board.getTitle(), board.getContent(), board.getWriter());
        boardMap.put(boardSeq, board);
        boardSeq++;
        return board;
    }

    public Board boardById(Long id){
        for (Board board : boardMap.values()) {
            if (board.getId().equals(id)) {
                return board;
            }
        }
        return null;
    }
}
