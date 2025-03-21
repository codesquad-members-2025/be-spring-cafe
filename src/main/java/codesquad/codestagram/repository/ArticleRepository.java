package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Optional<Article> findByArticleId(Long id);

    List<Article> findAllArticles();
}
