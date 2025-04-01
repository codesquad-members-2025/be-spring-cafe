package codesquad.codestagram;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.SpringDataJpaArticleRepository;
import codesquad.codestagram.service.ArticleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ArticleServiceIntegrationTest {

    @Autowired
    ArticleService articleService;
    @Autowired
    SpringDataJpaArticleRepository articleRepository;

    @Test
    @DisplayName("생성한 게시글과 찾은 게시글이 동일한 id를 가지고 있는지 테스트")
    public void ask() throws Exception {
        // given
        User user = new User("userId", "name", "password", "email");
        Article newArticle = new Article(user, "title", "contents");

        // when
        Long articleId = articleService.save(newArticle);

        // then
        Article findArticle = articleRepository.findById(articleId).get();
        assertThat(newArticle.getTitle()).isEqualTo(findArticle.getTitle());
    }
}
