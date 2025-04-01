package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.ReplyApiController.NEED_LOGIN;
import static codesquad.codestagram.service.ArticleService.NO_ARTICLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.ReplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReplyApiController.class)
class ReplyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ArticleService articleService;
    @MockitoBean
    private ReplyService replyService;
    @MockitoBean
    private AuthService authService;
    protected MockHttpSession session = new MockHttpSession();

    @Test
    @DisplayName("로그인이 실패하면 UNAUTHORIZED status가 반환됩니다.")
    void replyLoginTest() throws Exception {
        //given
        given(authService.checkLogin(session)).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(post("/api/reply/{articleId}", 1)
                .param("content", "content")
                .session(session));

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().string(NEED_LOGIN));
    }
    @Test
    @DisplayName("댓글을 추가할 게시글이 없는 경우 NOT_FOOUND status가 반환됩니다.")
    void replyNoArticleTest() throws Exception {
        //given
        given(authService.checkLogin(session)).willReturn(false);
        given(articleService.findArticleById(1L)).willThrow(new IllegalArgumentException(NO_ARTICLE));

        //when
        ResultActions result = mockMvc.perform(post("/api/reply/{articleId}", 1)
                .param("content", "content")
                .session(session));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(NO_ARTICLE));
    }
    @Test
    @DisplayName("댓글 추가를 성공할 경우 OK status와 저장한 댓글을 리턴한다.")
    void addReplyTest() throws Exception {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("title", "testContent", user);
        Reply mockReply = new Reply("content", article, user);

        ReflectionTestUtils.setField(article, "id", 1L);
        session.setAttribute(SESSIONED_USER, user);
        given(authService.checkLogin(session)).willReturn(false);
        given(articleService.findArticleById(1L)).willReturn(article);
        given(replyService.addReply(anyString(), any(Article.class), any(User.class))).willReturn(mockReply);

        //when
        ResultActions result = mockMvc.perform(post("/api/reply/{articleId}", 1)
                .param("content", "content")
                .session(session));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.articleId").value("1"))
                .andExpect(jsonPath("$.userId").value("testUser"));
    }
}