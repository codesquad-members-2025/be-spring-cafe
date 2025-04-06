package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.replies")
    List<Article> findAllWithReplies();

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.replies r LEFT JOIN FETCH r.writer WHERE a.id = :id")
    Optional<Article> findByIdWithReplies(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.writer LEFT JOIN FETCH a.replies WHERE a.deleted = false")
    List<Article> findAllWithRepliesAndWriter();

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.writer LEFT JOIN FETCH a.replies r LEFT JOIN FETCH r.writer WHERE a.id = :id AND a.deleted = false")
    Optional<Article> findByIdWithRepliesAndWriter(@Param("id") Long id);
}
