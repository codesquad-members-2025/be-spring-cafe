package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class MemoryArticleRepository implements ArticleRepository{
    private static final ArrayList<Article> articleStore = new ArrayList<>();

    @Override
    public Article save(Article article) {
        articleStore.add(article);
        return article;
    }
}
