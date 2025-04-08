package codesquad.codestagram.domain.article;

import codesquad.codestagram.domain.article.exception.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.Reply;
import codesquad.codestagram.domain.reply.ReplyRepository;
import codesquad.codestagram.domain.user.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ArticleService articleService;

    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // given: 테스트용 User 생성 후 Reflection을 통해 ID 할당
        user = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        setField(user, "id", 1L);

        // given: 테스트용 Article 생성 후 Reflection을 통해 ID 할당 (작성자 ID는 user.getId() 사용)
        article = new Article(user.getId(), "Title", "Content");
        setField(article, "id", 1L);
    }

    // Reflection을 이용해 private 필드에 값을 설정
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Reflection 실패: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("게시글 페이징: 30개의 게시글 중 첫 페이지에 15개가 반환된다.")
    void findArticles_with30Articles_shouldReturn15FirstPage() {
        // given: 30개의 게시글 생성
        List<Article> articles = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Article article = new Article(user.getId(), "Title" + i, "Content" + i);
            setField(article, "id", (long) i);
            articles.add(article);
        }

        // 첫 페이지에 해당하는 15개의 게시글을 subList로 추출
        Pageable pageable = PageRequest.of(0, 15, Sort.by("createdDate").descending());
        Page<Article> page = new PageImpl<>(articles.subList(0, 15), pageable, articles.size());

        when(articleRepository.findAll(any(Pageable.class))).thenReturn(page);

        // when: Pageable 객체를 이용해 페이지 0의 게시글 조회
        Page<Article> result = articleService.findArticles(pageable);

        // then: 반환된 게시글 수와 전체 게시글 수 확인
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getContent()).hasSize(15); // 첫 페이지에 15개 게시글
        softly.assertThat(result.getTotalElements()).isEqualTo(30); // 전체 게시글 수
        softly.assertThat(result.getNumber()).isEqualTo(0); // 첫 페이지
        softly.assertAll();
        verify(articleRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("유효한 사용자가 게시물을 작성할 경우, 저장된 게시물을 반환한다.")
    void createArticle_withValidUser_shouldReturnSavedArticle() {
        // given
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // when
        Article createdArticle = articleService.createArticle("Title", "Content", user);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(createdArticle).isNotNull();
        softly.assertThat(createdArticle.getTitle()).isEqualTo("Title");
        softly.assertAll();
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    @DisplayName("게시물이 존재할 경우, 해당 게시물을 반환한다.")
    void findArticle_whenArticleExists_shouldReturnArticle() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // when
        Article foundArticle = articleService.findArticle(1L);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(foundArticle).isEqualTo(article);
        softly.assertThat(foundArticle.getTitle()).isEqualTo("Title");
        softly.assertThat(foundArticle.getContent()).isEqualTo("Content");
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물이 존재하지 않을 경우, ArticleNotFoundException이 발생한다.")
    void findArticle_whenArticleNotExists_shouldThrowArticleNotFoundException() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleService.findArticle(1L))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage("게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("게시물이 존재하고, 사용자가 작성자와 일치할 경우, getAuthorizedArticle은 게시물을 반환한다.")
    void getAuthorizedArticle_whenArticleExistsAndUserMatches_shouldReturnArticle() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // when
        Article authorizedArticle = articleService.getAuthorizedArticle(1L, user);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(authorizedArticle).isEqualTo(article);
        softly.assertThat(authorizedArticle.getTitle()).isEqualTo("Title");
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물이 존재하지만, 사용자가 작성자와 일치하지 않을 경우, UnauthorizedException이 발생한다.")
    void getAuthorizedArticle_whenArticleExistsAndUserDoesNotMatch_shouldThrowUnauthorizedException() {
        // given: 다른 사용자 생성
        User anotherUser = new User("other", "test", "Other", "other@example.com");
        setField(anotherUser, "id", 2L);
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // when & then
        assertThatThrownBy(() -> articleService.getAuthorizedArticle(1L, anotherUser))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("본인이 작성한 글만 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("승인된 사용자가 게시물을 업데이트하면, 제목과 내용이 변경된다.")
    void updateArticle_whenAuthorized_shouldUpdateArticle() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // when
        Article updatedArticle = articleService.updateArticle(1L, "New Title", "New Content", user);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(updatedArticle.getTitle()).isEqualTo("New Title");
        softly.assertThat(updatedArticle.getContent()).isEqualTo("New Content");
        softly.assertAll();
    }

    @Test
    @DisplayName("승인된 사용자가 게시물을 삭제하면, 게시물이 삭제된다.")
    void deleteArticle_whenAuthorized_shouldDeleteArticle() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        List<Reply> replies = new ArrayList<>();
        Reply reply1 = new Reply(user, 1L, "Reply1");
        setField(reply1, "id", 2L);
        Reply reply2 = new Reply(user, 1L, "Reply2");
        setField(reply2, "id", 3L);
        replies.add(reply1);
        replies.add(reply2);

        when(replyRepository.findByArticleIdAndDeletedFalse(1L)).thenReturn(replies);
        when(replyRepository.saveAll(anyList())).thenReturn(replies);

        // when
        articleService.deleteArticle(1L, user);

        // then
        verify(replyRepository).saveAll(anyList());
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(article.isDeleted()).isTrue();
        // 모든 댓글이 soft delete 되었는지 검증
        for (Reply r : replies) {
            softly.assertThat(r.isDeleted()).isTrue();
        }
        softly.assertAll();
    }

}
