package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 테스트 클래스에 @transactional 붙이면, 각 테스트가 끝난 후 데이터가 롤백되어 DB가 깨끗하게 유지됨
 * 테스트를 위한거라, 비지니스 로직의 ㅡ랜잭션 처리와는 별개.
 */
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("회원가입이 정삭적으로 되는지 테스트.")
    public void 회원가입() throws Exception {
        //Given
        User user = new User();
        user.setLoginId("brie822");
        user.setName("브리");
        user.setPassword("123456");
        user.setEmail("brie822@gmail.com");

        //when
        Long id = userService.join(user);

        //then
        Optional<User> findUser = userService.findOne(id);
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getLoginId()).isEqualTo("brie822");
        assertThat(findUser.get().getName()).isEqualTo("브리");
        assertThat(findUser.get().getPassword()).isEqualTo("123456");
        assertThat(findUser.get().getEmail()).isEqualTo("brie822@gmail.com");
    }

    @Test
    @DisplayName("중복 loginId로 회원가입 하면 예외 발생하는지 테스트.")
    void 죽복_회원_예외() {
        //given
        User user1 = new User();
        user1.setLoginId("gyuwon");

        User user2 = new User();
        user2.setLoginId("gyuwon");

        // When
        userService.join(user1);

        // Then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(user2));
        assertThat(e.getMessage()).isEqualTo("User already exists");

    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void 회원정보_수정_성공() {
        // Given
        User user = new User();
        user.setLoginId("brie822");
        user.setName("브리");
        user.setPassword("123456");
        user.setEmail("brie822@email.com");
        Long id = userService.join(user);

        // When
        boolean result = userService.updateUser(
                id,
                "123456", // 현재 비밀번호 일치
                "abcd",
                "규원",
                "brie822@gmail.com"
        );

        // Then
        assertThat(result).isTrue();

        Optional<User> updated = userService.findOne(id);
        assertThat(updated).isPresent();
        assertThat(updated.get().getPassword()).isEqualTo("abcd");
        assertThat(updated.get().getName()).isEqualTo("규원");
        assertThat(updated.get().getEmail()).isEqualTo("brie822@gmail.com");
    }

    @Test
    @DisplayName("비밀번호 틀리면 회원 수정 실패 테스트")
    void 회원정보_수정_실패_비밀번호틀림() {
        // Given
        User user = new User();
        user.setLoginId("brie822");
        user.setName("브리");
        user.setPassword("123456");
        user.setEmail("brie822@email.com");
        Long id = userService.join(user);

        // When
        boolean result = userService.updateUser(id, "abcd", "abcedf", "브리", "brie822@email.com");

        // Then
        assertThat(result).isFalse(); // 수정 실패
        User updated = userService.findOne(id).get();
        assertThat(updated.getPassword()).isEqualTo("123456"); // 안 바뀜
    }

}
