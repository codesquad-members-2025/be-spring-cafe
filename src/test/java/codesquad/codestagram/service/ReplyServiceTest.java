package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ReplyRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class ReplyServiceTest {
    @MockitoBean
    private ReplyRepository replyRepository;
    @Autowired
    private ReplyService replyService;

    private User user;
    private User user2;
    private Article article;
    private HttpSession mockSession;
    @BeforeEach
    public void init() {
        user = new User("testUser", "password123", "test", "test@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        user2 = new User("testUser2", "password123", "test", "test@example.com");
        ReflectionTestUtils.setField(user2, "id", 2L);

        article = new Article("test", "testContent", user);

        mockSession = new MockHttpSession();
        mockSession.setAttribute(SESSIONED_USER, user);
    }
    @Test
    @DisplayName("댓글 작성자와 유저가 다르면 true를 리턴한다.")
    void replyAuthorTest() {
        //given
        Reply reply = new Reply("content", article, user);

        //when
        boolean notReplyAuthor = replyService.isNotReplyAuthor(user, reply);
        boolean notReplyAuthor2 = replyService.isNotReplyAuthor(user2, reply);

        //then
        assertThat(notReplyAuthor).isFalse();
        assertThat(notReplyAuthor2).isTrue();
    }

    @Test
    @DisplayName("댓글을 삭제하는 경우 isDeleted가 true가 되어야 한다.")
    void deleteReply() {
        //given
        Reply reply1 = new Reply("content1", article, user);
        Reply reply2 = new Reply("content2", article, user);
        Reply reply3 = new Reply("content3", article, user);


        //when
        replyService.deleteReply(reply1);
        replyService.deleteReply(reply2);

        //then
        assertThat(reply1.isDeleted()).isTrue();
        assertThat(reply2.isDeleted()).isTrue();
        assertThat(reply3.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("글 작성자는 댓글이 앖는 글을 삭제할 수 있다.")
    void deleteNoReplyArticle() {
        given(replyRepository.findAllByArticleNotDeleted(article)).willReturn(List.of());
        //when
        boolean canDelete = replyService.checkCanDelete(article, mockSession);
        //then
        assertThat(canDelete).isTrue();
    }

    @Test
    @DisplayName("다른 유저의 댓글이 있는 경우 글을 지울 수 없다.")
    void deleteOtherUserReplyArticle() {
        //given
        Reply reply1 = new Reply("content1", article, user);
        Reply reply2 = new Reply("content2", article, user);
        Reply reply3 = new Reply("content3", article, user2);

        given(replyRepository.findAllByArticleNotDeleted(article)).willReturn(List.of(reply1,reply2,reply3));
        //when
        boolean canDelete = replyService.checkCanDelete(article, mockSession);
        //then
        assertThat(canDelete).isFalse();
    }

    @Test
    @DisplayName("글 작성자의 댓글만 있다면 글을 삭제할 수 있다.")
    void deleteAuthorReplyArticle() {
        //given
        Reply reply1 = new Reply("content1", article, user);
        Reply reply2 = new Reply("content2", article, user);
        Reply reply3 = new Reply("content3", article, user);

        given(replyRepository.findAllByArticleNotDeleted(article)).willReturn(List.of(reply1,reply2,reply3));
        //when
        boolean canDelete = replyService.checkCanDelete(article, mockSession);
        //then
        assertThat(canDelete).isTrue();
    }
}