package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.ArticleController.ARTICLE;
import static codesquad.codestagram.controller.ArticleController.AUTHOR;
import static codesquad.codestagram.controller.ArticleController.CAN_NOT_DELETE;
import static codesquad.codestagram.controller.ArticleController.REPLIES;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;
import static codesquad.codestagram.service.ArticleService.NOT_AUTHOR;
import static codesquad.codestagram.service.ArticleService.NO_ARTICLE;
import static codesquad.codestagram.service.ArticleService.NO_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto.ArticleRequestDto;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.ReplyService;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

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
    @DisplayName("글을 작성하는 유저가 존재하지 않으면 에러가 발생한다.")
    void writeArticleNoUserTest() throws Exception {
        //given
        ArticleRequestDto articleRequestDto = new ArticleRequestDto("testTitle", "content", "testUser");
        willThrow(new IllegalArgumentException(NO_USER)).given(articleService).saveArticle(any(ArticleRequestDto.class));

        //when
        ResultActions result = mockMvc.perform(post("/articles")
                .param("title", "title")
                .param("content", "content")
                .param("userId", "userId")
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute(ERROR_MESSAGE, NO_USER))
                .andExpect(redirectedUrl("/articles/form"));
    }

    @Test
    @DisplayName("조회하려는 글이 없으면 에러가 발생한다.")
    void articleDetailNotEqualId() throws Exception {
        //given
        given(articleService.findArticleById(1L)).willThrow(new IllegalArgumentException(NO_ARTICLE));

        //when
        ResultActions result = mockMvc.perform(get("/articles/{articleId}", 1L)
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute(ERROR_MESSAGE, NO_ARTICLE));
    }

    @Test
    @DisplayName("글의 작성자가 아닌 경우 isArticleAuthor가 false이다.")
    void isArticleAuthorTest() throws Exception {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);
        given(articleService.findArticleById(1L)).willReturn(article);
        given(replyService.findReplies(article)).willReturn(List.of());;
        willThrow(new AccessDeniedException(NOT_AUTHOR)).given(articleService).matchArticleAuthor(session, article);

        //when
        ResultActions result = mockMvc.perform(get("/articles/{articleId}", 1L)
                .session(session));

        //then
        result.andExpect(status().isOk())
                .andExpect(model().attribute(AUTHOR, false))
                .andExpect(model().attribute(ARTICLE, article))
                .andExpect(model().attribute(REPLIES, List.of()))
                .andExpect(view().name("articles/show"));
    }

    @Test
    @DisplayName("글을 삭제하지 못하는 경우 에러 메세지와 함께 글의 상세 페이지로 되돌아간다.")
    void canNotDeleteArticleTest() throws Exception {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);
        ReflectionTestUtils.setField(article, "id", 1L);
        given(articleService.findArticleById(1L)).willReturn(article);
        willDoNothing().given(articleService).matchArticleAuthor(session, article);
        given(replyService.checkCanDelete(article, session)).willReturn(false);

        //when
        ResultActions result = mockMvc.perform(delete("/articles/{articleId}", 1L)
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute(ERROR_MESSAGE, CAN_NOT_DELETE))
                .andExpect(redirectedUrl("/articles/1"));
    }
}