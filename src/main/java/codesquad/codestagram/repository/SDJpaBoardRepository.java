package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SDJpaBoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByWriter(String writer);
    Optional<Board> findByTitle(String title);
}
