package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("boardJpaRepository")
public interface BoardRepository extends JpaRepository<Board, Long> {
}
