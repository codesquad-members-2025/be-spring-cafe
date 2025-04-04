package codesquad.codestagram.domain.reply;

import codesquad.codestagram.domain.user.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyService replyService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        setField(user, "id", 1L);
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

}
