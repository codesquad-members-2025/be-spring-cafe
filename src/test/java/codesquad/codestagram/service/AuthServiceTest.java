package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;

import codesquad.codestagram.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
class AuthServiceTest {

    MockHttpSession session = new MockHttpSession();
    @Autowired
    AuthService authService;
    @Test
    @DisplayName("세션에 저장되어 있는 user가 없다면 로그인 하지 않은 유저다")
    void checkNotLoginTest() {
        //when
        boolean isLogin = authService.checkLogin(session);

        //then
        Assertions.assertThat(isLogin).isTrue();
    }
    @Test
    @DisplayName("세션에 저장되어 있는 user가 없다면 로그인 하지 않은 유저다")
    void checkLoginTest() {
        //given
        session.setAttribute(SESSIONED_USER, new User("testUser", "password", "aaa", "test1@example.com"));
        //when
        boolean isLogin = authService.checkLogin(session);

        //then
        Assertions.assertThat(isLogin).isFalse();
    }
}