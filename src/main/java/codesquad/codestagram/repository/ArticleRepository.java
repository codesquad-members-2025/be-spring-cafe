package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a where a.isDeleted = false")
    List<Article> findAllIsNotDeleted();
}
