package codesquad.codestagram.domain.article;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {

    private final List<Article> articles;

    public ArticleRepository(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> findAll() {
        return articles;
    }

    public Optional<Article> findById(int id) {
        return articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst();
    }

    public void save(Article article) {
        articles.add(article);
    }

    public int size() {
        return articles.size();
    }

}
