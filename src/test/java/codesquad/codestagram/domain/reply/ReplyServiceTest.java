package codesquad.codestagram.domain.reply;

import codesquad.codestagram.domain.article.ArticleRepository;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.exception.ReplyNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ReplyService replyService;

    private User user;
    private Reply reply;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        setField(user, "id", 1L);

        reply = new Reply(user, 1L, "내용입니다.");
        setField(reply, "id", 10L);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Reflection 설정 실패", e);
        }
    }

    @Test
    @DisplayName("댓글 페이징: 15개의 댓글 중 첫 페이지에 5개가 반환된다.")
    void findRepliesByArticle_with15Replies_shouldReturn5Replies() {
        // given: 15개의 댓글 생성
        List<Reply> replies = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Reply reply = new Reply(user, 1L, "Reply" + i);
            setField(reply, "id", (long) i);
            replies.add(reply);
        }

        // 첫 페이지에 해당하는 5개의 댓글을 subList로 추출하고, Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Reply> page = new PageImpl<>(replies.subList(0, 5), pageable, replies.size());

        when(replyRepository.findByArticleIdAndDeletedFalse(anyLong(), any(Pageable.class))).thenReturn(page);

        // when
        Page<Reply> result = replyService.findRepliesByArticle(1L, pageable);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getContent()).hasSize(5);  // 첫 페이지에 5개 댓글
        softly.assertThat(result.getTotalElements()).isEqualTo(30);  // 전체 댓글 수 15개
        softly.assertThat(result.getNumber()).isEqualTo(0);  // 첫 페이지 (0부터 시작)
        softly.assertAll();
        verify(replyRepository).findByArticleIdAndDeletedFalse(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("댓글 등록 - 게시글이 존재하면 저장 성공")
    void addReply_whenArticleExists_shouldSaveReply() {
        // given
        when(articleRepository.existsById(anyLong())).thenReturn(true);
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        // when
        Reply result = replyService.addReply(1L, user, "새 댓글");

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getContent()).isEqualTo("내용입니다.");
        softly.assertThat(result.getUser()).isEqualTo(user);
        softly.assertThat(result.getArticleId()).isEqualTo(1L);
        softly.assertAll();
    }

    @Test
    @DisplayName("댓글 등록 - 게시글이 존재하지 않으면 예외 발생")
    void addReply_whenArticleDoesNotExist_shouldThrowException() {
        // given
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);
        when(articleRepository.existsById(anyLong())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> replyService.addReply(1L, user, "댓글"))
                .isInstanceOf(ReplyNotFoundException.class)
                .hasMessage("게시물이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글 삭제 - 작성자와 일치하면 soft delete 성공")
    void deleteReply_whenUserMatches_shouldDelete() {
        // given
        when(replyRepository.findById(10L)).thenReturn(Optional.of(reply));
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        // when
        replyService.deleteReply(10L, user);

        // then
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("댓글 삭제 - 작성자가 아니면 예외 발생")
    void deleteReply_whenUserNotMatches_shouldThrowUnauthorizedException() {
        // given
        User anotherUser = new User("other", "pw", "다른이", "other@mail.com");
        setField(anotherUser, "id", 2L);
        when(replyRepository.findById(10L)).thenReturn(Optional.of(reply));

        // when & then
        assertThatThrownBy(() -> replyService.deleteReply(10L, anotherUser))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("자신이 작성한 댓글만 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("댓글 삭제 - 존재하지 않으면 예외 발생")
    void deleteReply_whenNotFound_shouldThrowException() {
        // given
        when(replyRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> replyService.deleteReply(999L, user))
                .isInstanceOf(ReplyNotFoundException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
    }

}
