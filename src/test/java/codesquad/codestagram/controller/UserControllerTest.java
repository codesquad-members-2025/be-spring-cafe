package codesquad.codestagram.controller;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;
import static codesquad.codestagram.controller.UserController.ERROR_MESSAGE;
import static codesquad.codestagram.controller.UserController.NOT_EQUAL_ID;
import static codesquad.codestagram.controller.UserController.PASSWORD_VALID;
import static codesquad.codestagram.controller.UserController.USER;
import static codesquad.codestagram.service.ArticleService.NO_USER;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.AuthService;
import codesquad.codestagram.service.UserService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AuthService authService;
    MockHttpSession session = new MockHttpSession();


    @Test
    @DisplayName("모든 회원을 모델에 저장하고 회원 리스트 뷰를 리턴한다.")
    void showUserListTest() throws Exception {
        //given
        User user1 = new User("testUser1", "password1", "aaa", "test1@example.com");
        User user2 = new User("testUser2", "password2", "bbb", "test2@example.com");
        User user3 = new User("testUser3", "password3", "ccc", "test3@example.com");
        User user4 = new User("testUser4", "password4", "ddd", "test4@example.com");
        List<User> userList = List.of(user1, user2, user3, user4);
        given(userService.getUserList()).willReturn(userList);

        //when
        ResultActions result = mockMvc.perform(get("/users"));

        //then
        result.andExpect(view().name("user/list"))
                .andExpect(model().attribute(USER, userList))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 프로필 조회시 아이디가 존재하지 않으면 에러가 발생한다.")
    void showProfileErrorTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        given(userService.getUserById(1L)).willThrow(new IllegalArgumentException(NO_USER));

        //when
        ResultActions result = mockMvc.perform(get("/users/{id}", 1));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(ERROR_MESSAGE, NO_USER));
    }

    @Test
    @DisplayName("회원 프로필 조회시 아이디가 존재하면 모델에 회원 정보를 담는다.")
    void showProfileTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        given(userService.getUserById(1L)).willReturn(user);

        //when
        ResultActions result = mockMvc.perform(get("/users/{id}", 1));

        //then
        result.andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute(USER, user));
    }

    @Test
    @DisplayName("정보를 수정하려는 사람이 본인이 아닌 경우 에러를 발생시킨다.")
    void editUserErrorTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        given(authService.checkLogin(session)).willReturn(false);
        session.setAttribute(SESSIONED_USER, user);

        //when
        ResultActions result = mockMvc.perform(get("/users/{id}/update", 2L)
                .session(session));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(ERROR_MESSAGE, NOT_EQUAL_ID));
    }

    @Test
    @DisplayName("정보를 수정하려는 사람이 본인이면 수정 폼으로 이동시킨다.")
    void editUserTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        given(authService.checkLogin(session)).willReturn(false);
        session.setAttribute(SESSIONED_USER, user);

        //when
        ResultActions result = mockMvc.perform(get("/users/{id}/update", 1L)
                .session(session));

        //then
        result.andExpect(status().isOk())
                .andExpect(view().name("user/updateForm"))
                .andExpect(model().attribute(USER, user));
    }

    @Test
    @DisplayName("정보 수정시 비밀번호가 틀리면 PasswordValid가 false이다")
    void validPasswordFailTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userService.getUserById(1L)).willReturn(user);
        //when
        ResultActions result = mockMvc.perform(get("/users/verify_password")
                .param("password", "different")
                .param("id", "1"));

        //test
        result.andExpect(view().name("user/updateForm"))
                .andExpect(model().attribute(PASSWORD_VALID, false));
    }
    @Test
    @DisplayName("정보 수정시 비밀번호가 맞으면 PasswordValid가 true이다")
    void validPasswordTest() throws Exception {
        //given
        User user = new User("testUser", "password", "aaa", "test1@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userService.getUserById(1L)).willReturn(user);
        //when
        ResultActions result = mockMvc.perform(get("/users/verify_password")
                .param("password", "password")
                .param("id", "1"));

        //test
        result.andExpect(view().name("user/updateForm"))
                .andExpect(model().attribute(PASSWORD_VALID, true));
    }
}