package codesquad.codestagram.repository;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByBoard(Board board); //특정 게시글에 달린 모든 댓글 조회
    Optional<Reply> findByIdAndWriter(Long id, User writer); // DB 조회 기반 검증
}