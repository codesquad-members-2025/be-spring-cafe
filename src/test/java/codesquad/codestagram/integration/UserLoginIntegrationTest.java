package codesquad.codestagram.integration;


import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        // 테스트 데이터 추가
        if (userRepository.findByLoginId("javajigi").isEmpty()) {
            User user1 = new User();
            user1.setLoginId("javajigi");
            user1.setPassword("test");
            user1.setName("자바지기");
            user1.setEmail("javajigi@slipp.net");
            userRepository.save(user1);
        }
        if (userRepository.findByLoginId("sanjigi").isEmpty()) {
            User user2 = new User();
            user2.setLoginId("sanjigi");
            user2.setPassword("test");
            user2.setName("산지기");
            user2.setEmail("sanjigi@slipp.net");
            userRepository.save(user2);
        }
    }

    @Test
    public void testLoginSuccess() throws Exception {
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
    public void testLoginFailure() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/login")
                        .param("loginId", "javajigi")
                        .param("password", "wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login_failed"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        Object user = session.getAttribute("user"); //통의 Spring MVC에서는 요청만 해도 세션이 만들어질 수 있음(특히, getsession() 호출시)
        assertNull(user); // 이게 중요한 체크!
    }

    @Test
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