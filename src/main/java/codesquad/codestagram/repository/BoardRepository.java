package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.Board;
import codesquad.codestagram.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("boardJpaRepository")
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUser(User user);
}