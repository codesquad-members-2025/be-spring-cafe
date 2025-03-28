package codesquad.codestagram;

import codesquad.codestagram.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인 한 사용자만 게시글 폼에 접근할 수 있는지 테스트")
    public void onlyLoggedInUserCanAccessPostForm() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        User mockUser = new User("userId", "password", "name", "email@email.com");
        session.setAttribute("loginUser", mockUser);

        // when & then
        mockMvc.perform(get("/qna/form").session(session))
                .andExpect(status().isOk())     // 응답의 http 상태 코드가 200 OK 인지 확인
                .andExpect(view().name("qna/form"));
    }

    @Test
    @DisplayName("로그인 하지 않은 사용자가 게시글을 쓰려고 했을 때 로그인 페이지가 나오는지 테스트")
    public void redirectsToLoginFormWhenUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/qna/form"))
                .andExpect(status().is3xxRedirection())     // 302 redirect (3xx 대의 리다이렉트 계열인지 확인)
                .andExpect(redirectedUrl("/user/login"));
    }
}
