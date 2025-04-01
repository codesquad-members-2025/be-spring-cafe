package codesquad.codestagram.integration;


import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMVC 사용해서 시뮬레이션
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserLoginIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        if (userRepository.findByLoginId("javajigi").isEmpty()) {
            User user1 = new User("javajigi", "자바지기", "test", "javajigi@slipp.net");
            userRepository.save(user1);
        }
        if (userRepository.findByLoginId("sanjigi").isEmpty()) {
            User user2 = new User("sanjigi", "산지기", "test", "sanjigi@slipp.net");
            userRepository.save(user2);
        }
    }

    @Test
    @DisplayName("로그인 테스트 - 홈 화면으로")
    public void testLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/login")
                        .param("loginId", "javajigi")
                        .param("password", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();

        // 세션 검사
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);

        // 세션에 저장된 사용자 정보 확인
        User sessionUser = (User) session.getAttribute("user"); // 혹은 너가 저장한 이름 (예: "loginUser")
        assertNotNull(sessionUser);
        assertThat(sessionUser.getLoginId()).isEqualTo("javajigi");
    }

    @Test
    @DisplayName("로그인 실패 -> login_faild 페이지로")
    public void testLoginFailure() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/login")
                        .param("loginId", "javajigi")
                        .param("password", "wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login_failed"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        Object user = session.getAttribute("user"); //Spring MVC에서는 요청만 해도 세션이 만들어질 수 있음(특히, getsession() 호출시)
        assertNull(user); // 이게 중요한 체크!
    }

    @Test
    @DisplayName("프로필 조회 - 로그인한 사용자가 자신의 프로필 볼 때")
    public void testProfileWhenLoggedIn() throws Exception {
        User user = userRepository.findByLoginId("javajigi").get();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/users/profile").session(session))
                .andExpect(status().isOk()) //if정상 -> HTTP 상태 코드 200(OK)을 반환
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    @DisplayName("프로필 조회 - 로그인 안된 경우 -> 로그인 페이지로")
    public void testProfileWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    @DisplayName("개인정보 수정 - 로그인한 사용자가 다른 사용자의 정보를 수정하려 할 때 - 오류 팝업, 로그인 페이지로")
    public void testUpdateFormWhenNotAuthorized() throws Exception {
        User user = userRepository.findByLoginId("javajigi").get();
        MockHttpSession session1 = new MockHttpSession();
        session1.setAttribute("user", user);

        User other = userRepository.findByLoginId("sanjigi").get();
        MockHttpSession session2 = new MockHttpSession();
        session2.setAttribute("user", other);

        mockMvc.perform(get("/users/edit/{id}", user.getId()).session(session2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }


    @Test
    @DisplayName("로그아웃 테스트")
    public void testLogout() throws Exception {
        // 먼저 로그인
        MvcResult result = mockMvc.perform(post("/users/login")
                        .param("loginId", "javajigi")
                        .param("password", "test"))
                .andReturn();

        // 로그인 후 세션이 생성되었는지 확인
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false); //세션이 있으면 반환하고, 없으면 null 반
        assertNotNull(session);

        // 로그아웃 수행
        mockMvc.perform(post("/users/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}