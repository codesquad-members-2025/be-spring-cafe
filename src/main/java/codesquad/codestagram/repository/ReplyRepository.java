package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.article = :article and r.isDeleted = false")
    List<Reply> findAllByArticleNotDeleted(Article article);

}
