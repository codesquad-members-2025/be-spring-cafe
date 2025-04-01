package codesquad.codestagram.service;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.dto.ArticleDto.ArticleRequestDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ArticleIntegrationTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleService articleService;

    @Test
    @DisplayName("글을 작성하면 제목, 내용, 작성자가 저장되어야 한다.")
    void saveArticleTest() {
        // Given
        User user = new User("testUser", "password123", "test", "test@example.com");
        User savedUser = userRepository.save(user);

        ArticleDto.ArticleRequestDto requestDto = new ArticleRequestDto("test", "testContent", "testUser");
        // When
        articleService.saveArticle(requestDto);
        Article findArticle = articleRepository.findById(1L).get();

        // Then
        assertThat(findArticle.getTitle()).isEqualTo("test");
        assertThat(findArticle.getContent()).isEqualTo("testContent");
        assertThat(findArticle.getUser()).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("게시글 전체 조회시 저장된 모든 글들을 가져와야 한다.")
    void userListTest(){
        //given
        User user1 = new User("testUser1", "password1", "aaa", "test1@example.com");
        User user2 = new User("testUser2", "password2", "bbb", "test2@example.com");
        userRepository.save(user1);
        userRepository.save(user2);

        Article article1 = new Article("test1", "content1", user1);
        Article article2 = new Article("test2", "content2", user1);
        Article article3 = new Article("test3", "content3", user2);
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        //when
        List<Article> articles = articleService.findArticles();

        //then
        assertThat(articles.size()).isEqualTo(3);
        assertThat(articles).extracting(Article::getTitle).containsExactly("test1", "test2", "test3");
        assertThat(articles).extracting(Article::getContent).containsExactly("content1", "content2", "content3");
        assertThat(articles).extracting(Article::getUser).containsExactly(user1, user1, user2);
    }
    @Test
    @DisplayName("게시글 삭제 시 삭제상태가 true가 된다")
    void deleteArticleErrorTest() {
        //given
        User user1 = new User("testUser1", "password1", "aaa", "test1@example.com");
        Article article1 = new Article("test1", "content1", user1);
        Article save = articleRepository.save(article1);
        //when
        articleService.delete(save.getId());
        Article deletedArticle = articleRepository.findById(save.getId()).get();
        //then
        assertThat(deletedArticle.isDeleted()).isTrue();
    }
}
