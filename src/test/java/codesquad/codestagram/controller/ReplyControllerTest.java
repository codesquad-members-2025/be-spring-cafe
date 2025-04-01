package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;
import static codesquad.codestagram.service.ArticleService.NO_ARTICLE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ArticleService articleService;
    @MockitoBean
    private ReplyService replyService;
    @MockitoBean
    private AuthService authService;
    MockHttpSession session = new MockHttpSession();
    @BeforeEach
    void init() {
        given(authService.checkLogin(session)).willReturn(false);
    }

    @Test
    @DisplayName("댓글을 작성하려는 글이 없는 경우 에러가 발생한다.")
    void addReplyErrorTest() throws Exception {
        //given
        given(articleService.findArticleById(1L)).willThrow(new IllegalArgumentException(NO_ARTICLE));

        //when
        ResultActions result = mockMvc.perform(post("/reply/{articleId}", 1L)
                        .param("content", "content")
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute(ERROR_MESSAGE, NO_ARTICLE));
    }

    @Test
    @DisplayName("삭제하려는 댓글이 존제하지 않으면 에라가 발생한다.")
    void deleteReplyNotExistTest() throws Exception {
        //given
        given(replyService.findReplyByIdAndNotDeleted(1L)).willThrow(new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        //when
        ResultActions result = mockMvc.perform(delete("/article/{articleId}/reply/{replyId}", 1L, 1L)
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/1"))
                .andExpect(flash().attribute(ERROR_MESSAGE, "댓글을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("삭제하려는 댓글의 작성자가 아닌 경우 에러가 발생한다.")
    void deleteReplyNotAuthorTest() throws Exception {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);
        Reply reply = new Reply("content", article, user);

        session.setAttribute(SESSIONED_USER, user);
        given(replyService.findReplyByIdAndNotDeleted(1L)).willReturn(reply);
        given(replyService.isNotReplyAuthor(user, reply)).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(delete("/article/{articleId}/reply/{replyId}", 1L, 1L)
                .session(session));
        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/1"))
                .andExpect(flash().attribute(ERROR_MESSAGE, "작성자만 댓글을 지울 수 있습니다."));
    }
}