package codesquad.codestagram.reply.repository;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("SELECT r FROM Reply r WHERE r.article = :article AND r.deleted = false")
    List<Reply> findAllByArticleAndNotDeleted(@Param("article") Article article);
}
