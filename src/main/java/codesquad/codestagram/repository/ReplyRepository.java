package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByArticle(Article article);
}
