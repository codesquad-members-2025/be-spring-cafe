package codesquad.codestagram;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.SpringDataJpaUserRepository;
import codesquad.codestagram.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    SpringDataJpaUserRepository userRepository;

    @Test
    @DisplayName("생성한 회원과 찾은 회원이 동일한 이름을 가지고 있는지 테스트")
    public void join() throws Exception {
        // given
        User user = new User("user", "name", "password", "email");

        // when
        String saveId = userService.join(user);

        // then
        User findUser = userRepository.findByUserId(saveId).get();
        assertThat(user.getName()).isEqualTo(findUser.getName());
    }

    @Test
    @DisplayName("중복 회원일 때 예외가 발생하는가")
    public void duplication() throws Exception {
        // given
        User user1 = new User("userId", "name", "password", "email");
        User user2 = new User("userId", "name", "password", "email");

        // when
        userService.join(user1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(user2));

        assertThat(e.getMessage()).isEqualTo("사용할 수 없는 아이디입니다.");
    }
}
