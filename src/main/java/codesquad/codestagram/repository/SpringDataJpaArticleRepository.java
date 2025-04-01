package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaArticleRepository extends JpaRepository<Article, Long> {
}
