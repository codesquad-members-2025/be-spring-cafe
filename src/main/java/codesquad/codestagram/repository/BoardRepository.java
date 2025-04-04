package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByWriter(User writer);
    Optional<Board> findByTitle(String title);
    List<Board> findByDeletedFalse(); //소프트 삭제되지 앟은 게시글만 반환
    Optional<Board> findByBoardIdAndWriter(Long boardId, User writer);
}
