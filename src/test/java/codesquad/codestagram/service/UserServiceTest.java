package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.repository.MemoryUserRepositoryTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    UserService userService;
    MemoryUserRepository userRepository; //clear 해주기 위해

    @BeforeEach
    public void beforeEach() {
        userRepository = new MemoryUserRepository();
        userService = new UserService(userRepository);
    }

    @AfterEach
    public void afterEach() {
        userRepository.clearStore();
    }

    @Test
    public void 회원가입() throws Exception {
        //Given
        User user = new User();
        user.setName("gyuwon");

        //when
        Long saveId = userService.join(user);

        //Then
        User FindUser = userRepository.findById(saveId).get();
        assertThat(user.getName()).isEqualTo(FindUser.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //assertThrows 사용

        //given
        User user1 = new User();
        user1.setName("gyuwon");

        User user2 = new User();
        user2.setName("gyuwon");

        //when
        userService.join(user1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(user2));
        assertThat(e.getMessage()).isEqualTo("User already exists");
    }
    @Test
    void findUser() {
    }

    @Test
    void findOne() {
    }
}
