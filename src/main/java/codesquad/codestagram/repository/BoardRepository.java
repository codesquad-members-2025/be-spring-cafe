package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findByBoardId(Long boardId);
    List<Board> findAll();
}
