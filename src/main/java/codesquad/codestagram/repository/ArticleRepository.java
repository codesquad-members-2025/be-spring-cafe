package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;

import java.util.List;

public interface ArticleRepository {

    Article save(Article article);

    List<Article> findAllArticles();
}
