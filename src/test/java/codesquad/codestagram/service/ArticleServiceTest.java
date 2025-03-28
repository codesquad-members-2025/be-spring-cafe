package codesquad.codestagram.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto.ArticleRequestDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ArticleServiceTest {

    @MockitoBean
    private ArticleRepository articleRepository;
    @MockitoBean
    private UserRepository userRepository;
    @Autowired
    private ArticleService articleService;

    @Test
    @DisplayName("게시글을 작성할 때 회원이 존재 하지 않으면 에러가 발생한다.")
    void writeArticleErrorTest() {
        //given
        given(userRepository.findByUserId("testUser")).willReturn(Optional.empty());
        ArticleRequestDto articleRequestDto = new ArticleRequestDto("testTitle", "content", "testUser");

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> articleService.saveArticle(articleRequestDto));
        //then
        assertThat(exception.getMessage()).isEqualTo("유저가 존재하지 않습니다.");

    }


    @Test
    @DisplayName("게시글 전체 조회시 저장된 모든 글들을 가져와야 한다.")
    void userListTest(){
        //given
        User user1 = new User("testUser1", "password1", "aaa", "test1@example.com");
        User user2 = new User("testUser2", "password2", "bbb", "test2@example.com");

        Article article1 = new Article("test1", "content1", user1);
        Article article2 = new Article("test2", "content2", user1);
        Article article3 = new Article("test3", "content3", user2);
        List<Article> articleList = List.of(article1, article2, article3);
        given(articleRepository.findAllIsNotDeleted()).willReturn(articleList);

        //when
        List<Article> articles = articleService.findArticles();

        //then
        assertThat(articles.size()).isEqualTo(3);
        assertThat(articles).extracting(Article::getTitle).containsExactly("test1", "test2", "test3");
        assertThat(articles).extracting(Article::getContent).containsExactly("content1", "content2", "content3");
        assertThat(articles).extracting(Article::getUser).containsExactly(user1, user1, user2);
    }
    @Test
    @DisplayName("게시글 내용 수정시 제목과 내용이이 수정되어야한다.")
    void updateArticleTest(){
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);

        given(userRepository.findByUserId("testUser")).willReturn(Optional.of(user));
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));

        //when
        articleService.updateArticle(1L, "updateTitle", "updateContent");

        //then
        assertThat(article.getTitle()).isEqualTo("updateTitle");
        assertThat(article.getContent()).isEqualTo("updateContent");
        verify(articleRepository).save(article);
    }

    @Test
    @DisplayName("게시글 삭제가 되면 isDeleted를 true로 바꾼다.")
    void deleteArticleTest() {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));

        //when
        articleService.delete(1L);
        Article deletedArticle = articleService.findArticleById(1L);
        //then
        assertThat(deletedArticle.isDeleted()).isTrue();
    }
}