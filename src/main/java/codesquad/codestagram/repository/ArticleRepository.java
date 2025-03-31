package codesquad.codestagram.repository;


import codesquad.codestagram.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findArticleById(Long id);

    Optional<Article> deleteArticleById(Long id);

    // 삭제된 데이터도 포함하여 조회
    @Query("SELECT a FROM Article a WHERE a.id = :id")
    Optional<Article> findByIdIncludingDeleted(@Param("id") Long id);
}
