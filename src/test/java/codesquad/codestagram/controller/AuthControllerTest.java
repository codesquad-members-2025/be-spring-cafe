package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.FAIL_SING_IN;
import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.AuthController.USER_ALREADY_EXIST;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    MockHttpSession session = new MockHttpSession();
    @Test
    @DisplayName("회원가입시 이미 같은아이디가 존재할경우 에러 메세지를 리턴한다.")
    void registerSameUserIdTest() throws Exception {
        //given
        given(userService.checkEqualUserId("existingUser")).willReturn(true);
        //when
        ResultActions result = mockMvc.perform(post("/users")
                .param("userId", "existingUser")
                .param("password", "password123")
                .param("name", "Test User")
                .param("email", "test@example.com"));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute(ERROR_MESSAGE, USER_ALREADY_EXIST));

    }
    @Test
    @DisplayName("회원가입시 같은아이디가 없는 경우 회원가입 성공한다.")
    void registerTest() throws Exception {
        //given
        given(userService.checkEqualUserId("testUser")).willReturn(false);
        //when
        ResultActions result = mockMvc.perform(post("/users")
                .param("userId", "testUser")
                .param("password", "password123")
                .param("name", "Test User")
                .param("email", "test@example.com"));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

    }

    @Test
    @DisplayName("로그인시 아이디와 비밀번호가 일치 하지 않는다면 에러 메세지를 출력한다.")
    void loginFailTest() throws Exception {
        //given
        given(userService.getUserForLogin("userId", "password")).willReturn(Optional.empty());

        //when
        ResultActions result = mockMvc.perform(post("/users/sign-in")
                .param("userId", "userId")
                .param("password", "password"));
        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(view().name("user/signIn"))
                .andExpect(model().attribute(ERROR_MESSAGE, FAIL_SING_IN));
    }

    @Test
    @DisplayName("로그인이 성공하면 세션에 로그인한 유저를 등록한다.")
    void loginTest() throws Exception {
        //given
        User mockUser = new User("testUser", "password", "Test Name", "test@example.com");
        given(userService.getUserForLogin("testUser", "password")).willReturn(Optional.of(mockUser));

        //when
        ResultActions result = mockMvc.perform(post("/users/sign-in")
                .param("userId", "testUser")
                .param("password", "password")
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(session.getAttribute(SESSIONED_USER)).isEqualTo(mockUser);
    }

    @Test
    @DisplayName("로그아웃시 세션의 정보를 삭제한다.")
    void logoutTest() throws Exception {
        //given
        User mockUser = new User("testUser", "password", "Test Name", "test@example.com");
        given(userService.getUserForLogin("testUser", "password")).willReturn(Optional.of(mockUser));
        session.setAttribute(SESSIONED_USER, mockUser);
        //when
        ResultActions result = mockMvc.perform(post("/users/sign-out")
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThatThrownBy(() -> session.getAttribute(SESSIONED_USER))
                .isInstanceOf(IllegalStateException.class)
                        .hasMessage("The session has already been invalidated");
    }
}