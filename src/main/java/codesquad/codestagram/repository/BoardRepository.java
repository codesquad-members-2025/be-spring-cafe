package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByWriter(User writer);
    Optional<Board> findByTitle(String title);
}
