package codesquad.codestagram.service;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserDto.UserRequestDto;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 가입시 중복된 아이디가 없으면 가입에 성공한다.")
    void registerUserTest() {
        // Given
        UserRequestDto requestDto = new UserRequestDto("testUser", "password123", "test", "test@example.com");

        // When
        userService.checkEqualUserId(requestDto.getUserId());
        userService.joinUser(requestDto);
        User findUser = userRepository.findByUserId("testUser").get();

        // Then
        assertThat(findUser.getUserId()).isEqualTo("testUser");
        assertThat(findUser.getPassword()).isEqualTo("password123");
        assertThat(findUser.getName()).isEqualTo("test");
        assertThat(findUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("회원 정보 수정시 이름과 이메일이 수정되어야한다.")
    void updateUserTest(){
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        User saved = userRepository.save(user);

        //when
        userService.updateUser(saved.getId(), "changeUser", "change@A.com");
        User changedUser = userRepository.findByUserId("testUser").get();
        //then
        assertThat(changedUser.getName()).isEqualTo("changeUser");
        assertThat(changedUser.getEmail()).isEqualTo("change@A.com");
    }
}
