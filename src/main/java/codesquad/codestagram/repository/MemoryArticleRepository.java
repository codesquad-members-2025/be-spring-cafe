package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryArticleRepository implements ArticleRepository {

    private final List<Article> articleMemory = new ArrayList<>();

//    private static int nextId = 1;

    @Override
    public Article save(Article article) {
        article.setId(articleMemory.size() + 1);
        articleMemory.add(article);
        return article;
    }

    @Override
    public Optional<Article> findByArticleId(int id) {
        return Optional.of(articleMemory.get(id - 1));
    }

    @Override
    public List<Article> findAllArticles() {
        return articleMemory;
    }
}
