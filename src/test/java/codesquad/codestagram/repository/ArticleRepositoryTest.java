package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

public class ArticleRepositoryTest {
    ArticleRepository articleRepository = new MemoryArticleRepository();

    @AfterEach
    void tearDown() {
        articleRepository.clearStore();
    }

    @Test
    @DisplayName("게시물을 생성하고 저장소에 추가할 수 있다.")
    public void save_article() {
        User user = new User("jd", "dino", "123", "jd@123");
        Article article = new Article(user,"title","content",1);
        articleRepository.save(article);
        List<Article> articles = articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getUser()).isEqualTo(user);
        assertThat(articles.get(0).getTitle()).isEqualTo("title");
        assertThat(articles.get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("해당 유저가 작성한 게시물을 찾을 수 있다.")
    public void find_article_by_user() {
        User user = new User("jd", "dino", "123", "jd@123");
        Article article = new Article(user,"title","content",1);
        articleRepository.save(article);
        List<Article> foundArticle = articleRepository.findByUser(user);
        assertThat(foundArticle.get(0).getUser()).isEqualTo(user);
        assertThat(foundArticle.get(0).getTitle()).isEqualTo("title");
        assertThat(foundArticle.get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("제목으로 게시물의 목록을 찾을 수 있다.")
    public void find_article_by_title() {
        User user = new User("jd", "dino", "123", "jd@123");
        Article article1 = new Article(user,"title","content",1);
        Article article2 = new Article(user,"title","content1",2);
        Article article3 = new Article(user,"title","content2",3);
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        List<Article> foundArticles = articleRepository.findByTitle("title");
        assertThat(foundArticles.size()).isEqualTo(3);
        assertThat(foundArticles.get(0).getContent()).isEqualTo("content");
        assertThat(foundArticles.get(1).getContent()).isEqualTo("content1");
        assertThat(foundArticles.get(2).getContent()).isEqualTo("content2");
    }
}
