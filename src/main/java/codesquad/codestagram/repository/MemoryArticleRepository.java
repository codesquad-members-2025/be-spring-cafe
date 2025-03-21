package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryArticleRepository implements ArticleRepository {

    private final Map<String, Article> articleMemory = new HashMap<>();

    @Override
    public Article save(Article article) {
        articleMemory.put(article.getWriter(), article);
        return article;
    }

    @Override
    public Optional<Article> findByWriter(String writer) {
        return Optional.of(articleMemory.get(writer));
    }

    @Override
    public List<Article> findAllArticles() {
        return new ArrayList<>(articleMemory.values());

    }
}
