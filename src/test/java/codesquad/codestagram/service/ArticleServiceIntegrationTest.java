package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ArticleServiceIntegrationTest {
    @Autowired private ArticleService articleService;
    @Autowired private UserService userService;

    @Test
    @DisplayName("회원가입 하고 글을 작성할 수 있다.")
    public void add_article() {
        User user = new User("dino","name", "pw","email@email.com");
        userService.join(user);
        Article article = new Article(user,"art","hello");
        articleService.saveArticle(article);

        assertThat(articleService.findAllArticles().get(0)).isEqualTo(article);
    }

    @Test
    @DisplayName("article을 작성할 때마다 id가 1씩 증가한다.")
    public void check_article_id() {
        User user = new User("dino","name", "pw","email@email.com");
        userService.join(user);
        Article article1 = new Article(user,"art1","hello");
        articleService.saveArticle(article1);
        Article article2 = new Article(user,"art2","hello");
        articleService.saveArticle(article2);

        assertThat(articleService.findAllArticles().get(0).getId()).isEqualTo(1);
        assertThat(articleService.findAllArticles().get(1).getId()).isEqualTo(2);
    }
}
