package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);
    List<Article> findByUser(User user);
    List<Article> findByTitle(String title);
    Optional<Article> findById(int id);
    List<Article> findAll();
    void delete(Article article);
}
