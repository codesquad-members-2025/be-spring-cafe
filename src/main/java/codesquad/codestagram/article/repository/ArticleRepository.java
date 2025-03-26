package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findArticleById(Long id);
}
