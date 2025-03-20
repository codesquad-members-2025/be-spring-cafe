package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.MemoryArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class ArticleServiceTest {
    ArticleRepository articleRepository = new MemoryArticleRepository();
    ArticleService articleService = new ArticleService(articleRepository);

    @AfterEach
    void tearDown() {
        articleRepository.clearStore();
    }

    @Test
    @DisplayName("article을 추가하고 모든 article 목록을 가져올 수 있다.")
    void add_article(){
        User user = new User("jdragon","dino","1234","aa@aa");
        Article article = new Article(user, "title", "content",1);
        articleService.saveArticle(article);
        List<Article> articles= articleService.findAllArticles();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0)).isEqualTo(article);
    }

    @Test
    @DisplayName("해당하는 user 의 article을 가져올 수 있다. ")
    void find_article_by_user(){
        User user1 = new User("jdragon","dino","1234","aa@aa");
        User user2 = new User("jdragon2","dino2","12342","aa@aa2");
        Article article1 = new Article(user1, "title", "content",1);
        Article article2 = new Article(user1, "title", "content1",2);
        Article article3 = new Article(user2, "title", "content1",3);
        articleService.saveArticle(article1);
        articleService.saveArticle(article2);
        articleService.saveArticle(article3);
        List<Article> articles= articleService.findArticlesByUser(user1);
        assertThat(articles.size()).isEqualTo(2);
        assertThat(articles.get(0)).isEqualTo(article1);
        assertThat(articles.get(1)).isEqualTo(article2);
    }

    @Test
    @DisplayName("해당하는 title 의 article을 가져올 수 있다. ")
    void find_article_by_title(){
        User user = new User("jdragon","dino","1234","aa@aa");
        Article article1 = new Article(user, "title1", "content",1);
        Article article2 = new Article(user, "title1", "content",2);
        Article article3 = new Article(user, "title2", "content",3);
        articleService.saveArticle(article1);
        articleService.saveArticle(article2);
        articleService.saveArticle(article3);

        List<Article> articles= articleService.findArticlesByTitle("title1");
        assertThat(articles.size()).isEqualTo(2);
        assertThat(articles.get(0)).isEqualTo(article1);
        assertThat(articles.get(1)).isEqualTo(article2);
    }

}
