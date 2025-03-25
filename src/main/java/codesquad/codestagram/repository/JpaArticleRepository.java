package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaArticleRepository extends ArticleRepository, JpaRepository<Article, Integer> {

    @Override
    Article save(Article article);

    @Override
    List<Article> findByUser(User user);

    @Override
    List<Article> findByTitle(String title);

    @Override
    Optional<Article> findById(int id);

    @Override
    List<Article> findAll();
}
