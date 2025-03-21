package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryArticleRepository implements ArticleRepository {

    private final Map<Long, Article> articleMemory = new HashMap<>();

    @Override
    public Article save(Article article) {
        articleMemory.put(article.getId(), article);
        return article;
    }

    @Override
    public Optional<Article> findByArticleId(Long id) {
        return Optional.of(articleMemory.get(id));
    }

    @Override
    public List<Article> findAllArticles() {
        return new ArrayList<>(articleMemory.values());

    }
}
