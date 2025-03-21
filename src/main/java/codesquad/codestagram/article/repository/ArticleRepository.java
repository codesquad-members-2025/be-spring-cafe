package codesquad.codestagram.article.repository;

import java.util.Optional;

public interface ArticleRepository {
    void save(Atricle atricle);

    Optional<Article> findById();

    List<Article> findAll();
}
