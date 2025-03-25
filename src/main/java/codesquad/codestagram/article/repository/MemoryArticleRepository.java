package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryArticleRepository implements ArticleRepository{
    private static final ArrayList<Article> articleStore = new ArrayList<>();

    @Override
    public Article save(Article article) {
        int id = articleStore.size()+1;
        articleStore.add(new Article(id, article.getWriter(), article.getTitle(), article.getContents()));
        return article;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleStore;
    }

    @Override
    public Article findById(int id) {
        return articleStore.get(id-1);
    }


}
