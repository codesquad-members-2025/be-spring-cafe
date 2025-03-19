package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;

import java.util.List;

public interface ArticleRepository {
    Article save(Article article);
    List<Article> findByUser(User user);
    List<Article> findByTitle(String title);
    List<Article> findAll();
    void clearStore();
    int getStoreSize();
}
