package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryArticleRepository implements ArticleRepository {

    private final Map<String, Article> articleMemory = new HashMap<>();

    @Override
    public Article save(Article article) {
        articleMemory.put(article.getTitle(), article);
        return article;
    }

    @Override
    public List<Article> findAllArticles() {
        return new ArrayList<>(articleMemory.values());

    }
}
