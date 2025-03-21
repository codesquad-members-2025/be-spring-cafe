package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.dto.UserForm;
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
    @DisplayName("회원이 글을 쓰고, 해당하는 회원의 글을 가져올 수 있다.")
    public void add_article() {
        UserForm userForm = new UserForm();
        userForm.setUserId("dino");
        userForm.setName("userName");

        User user = userService.join(userForm);

        ArticleForm articleForm = new ArticleForm("dino","art","hello");
        Article article = articleService.createArticleAndSave(articleForm);

        assertThat(articleService.findArticlesByUser(user).get(0)).isEqualTo(article);
    }

    @Test
    @DisplayName("article을 작성할 때마다 글의 개수가 증가한다.")
    public void check_article_id() {
        int articleSize = articleService.findAllArticles().size();
        UserForm userForm = new UserForm();
        userForm.setUserId("dino");
        userForm.setName("userName");
        User user = userService.join(userForm);
        ArticleForm articleForm1 = new ArticleForm("dino","art1","hello");
        ArticleForm articleForm2 = new ArticleForm("dino","art2","hello");

        Article article1 = articleService.createArticleAndSave(articleForm1);
        Article article2 = articleService.createArticleAndSave(articleForm2);

        assertThat(articleService.findAllArticles().size()).isEqualTo(articleSize + 2);
    }

    @Test
    @DisplayName("해당하는 title 의 article을 가져올 수 있다. ")
    public void get_article_by_title() {
        UserForm userForm = new UserForm();
        userForm.setUserId("dino");
        userForm.setName("userName");
        User user = userService.join(userForm);

        ArticleForm articleForm1 = new ArticleForm("dino","art1","hello");
        ArticleForm articleForm2 = new ArticleForm("dino","art2","hello");
        Article article1 = articleService.createArticleAndSave(articleForm1);
        Article article2 = articleService.createArticleAndSave(articleForm2);
        assertThat(articleService.findArticlesByTitle("art1").get(0)).isEqualTo(article1);
    }
}
