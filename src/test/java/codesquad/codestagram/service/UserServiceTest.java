package codesquad.codestagram.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserDto.UserRequestDto;
import codesquad.codestagram.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 가입시 중복된 아이디가 있으면 에러가 발생한다.")
    public void registerUserErrorTest() {
        // Given
        User user = new User("testUser", "password123", "test", "test@example.com");
        given(userRepository.findByUserId("testUser")).willReturn(Optional.of(user));

        // When
        UserRequestDto requestDto = new UserRequestDto("testUser", "12345", "AAA", "AAA@a.com");

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.joinUser(requestDto));

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 사용자 ID입니다.");
    }
}