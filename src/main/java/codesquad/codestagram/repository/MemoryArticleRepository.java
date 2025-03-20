package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository
public class MemoryArticleRepository implements ArticleRepository {
    private final List<Article> store = new ArrayList<>();

    @Override
    public Article save(Article article) {
        store.add(article);
        return article;
    }

    @Override
    public List<Article> findByUser(User user) {
        List<Article> foundArticles = new ArrayList<>();
        for(Article article : store) {
            if(article.getUser().equals(user)) {
                foundArticles.add(article);
            }
        }
        return foundArticles;
    }

    @Override
    public List<Article> findByTitle(String title) {
        List<Article> foundArticles = new ArrayList<>();
        for(Article article : store) {
            if(article.getTitle().equals(title)) {
                foundArticles.add(article);
            }
        }
        return foundArticles;
    }

    @Override
    public Optional<Article> findById(int id) {
        for(Article article : store) {
            if(article.getId() == id) {
                return Optional.of(article);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(store);
    }

//    @Override
//    public void clearStore() {
//        store.clear();
//    }
//
//    @Override
//    public int getStoreSize(){
//        return store.size();
//    }
}
