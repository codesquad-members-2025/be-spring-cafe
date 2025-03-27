package codesquad.codestagram.domain.user;

import codesquad.codestagram.domain.auth.UnauthorizedException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // //given: 테스트용 User 생성 (id setter가 없으므로 Reflection 사용)
        user = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        setField(user, "id", 1L);
    }

    // Reflection을 이용해 private 필드에 값을 설정하는 헬퍼 메서드
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Reflection 실패: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("유효한 회원가입 요청 시, 사용자가 저장된다.")
    void signUp_withValidInput_shouldSaveUser() {
        // given
        when(userRepository.findByUserId("javajigi")).thenReturn(Optional.empty());

        // when
        userService.signUp("javajigi", "test", "자바지기", "javajigi@slipp.net");

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 회원가입 요청 시, DuplicatedUserException이 발생한다.")
    void signUp_withDuplicateUser_shouldThrowException() {
        // given
        when(userRepository.findByUserId("javajigi")).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() ->
                userService.signUp("javajigi", "test", "자바지기", "javajigi@slipp.net")
        ).isInstanceOf(DuplicatedUserException.class)
                .hasMessage("이미 존재하는 사용자입니다.");
    }

    @Test
    @DisplayName("전체 사용자 목록 조회 시, 사용자 리스트가 반환된다.")
    void findAll_shouldReturnUserList() {
        // given
        User anotherUser = new User("other", "otherPass", "다른사용자", "other@example.com");
        setField(anotherUser, "id", 2L);
        List<User> users = Arrays.asList(user, anotherUser);
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> result = userService.findAll();

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).hasSize(2);
        softly.assertThat(result).containsExactly(user, anotherUser);
        softly.assertAll();
    }

    @Test
    @DisplayName("존재하는 사용자 ID로 프로필 조회 시, 해당 사용자가 반환된다.")
    void getUserProfile_withExistingId_shouldReturnUser() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserProfile(1L);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isEqualTo(user);
        softly.assertThat(result.getUserId()).isEqualTo("javajigi");
        softly.assertAll();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 프로필 조회 시, UserNotFoundException이 발생한다.")
    void getUserProfile_withNonExistingId_shouldThrowException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserProfile(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 업데이트 요청 시, UnauthorizedException이 발생한다.")
    void updateUser_withoutSessionUser_shouldThrowUnauthorizedException() {
        // when & then
        assertThatThrownBy(() ->
                userService.updateUser(1L, "new@example.com", "NewName", "test", "newPass", null)
        ).isInstanceOf(UnauthorizedException.class)
                .hasMessage("로그인이 필요합니다.");
    }

    @Test
    @DisplayName("세션 사용자와 대상 사용자가 일치하지 않으면, 업데이트 요청 시 UnauthorizedException이 발생한다.")
    void updateUser_withMismatchedUser_shouldThrowUnauthorizedException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User anotherUser = new User("other", "otherPass", "다른사용자", "other@example.com");
        setField(anotherUser, "id", 2L);

        // when & then
        assertThatThrownBy(() ->
                userService.updateUser(1L, "new@example.com", "NewName", "test", "newPass", anotherUser)
        ).isInstanceOf(UnauthorizedException.class)
                .hasMessage("본인의 정보만 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면, 업데이트 요청 시 UnauthorizedException이 발생한다.")
    void updateUser_withWrongPassword_shouldThrowUnauthorizedException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() ->
                userService.updateUser(1L, "new@example.com", "NewName", "wrongPassword", "newPass", user)
        ).isInstanceOf(UnauthorizedException.class)
                .hasMessage("비밀번호가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("유효한 업데이트 요청 시, 사용자의 이메일, 이름, 비밀번호가 업데이트된다.")
    void updateUser_withValidInput_shouldUpdateUser() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User updatedUser = userService.updateUser(1L, "new@example.com", "NewName", "test", "newPass", user);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
        softly.assertThat(updatedUser.getName()).isEqualTo("NewName");
        softly.assertThat(updatedUser.getPassword()).isEqualTo("newPass");
        softly.assertAll();
    }

}
