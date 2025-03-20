package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        this.softly = new SoftAssertions();
    }

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("testId라는 아이디를 가진 회원이 생성된다.")
    void save() {
        //Given
        User user = new User();
        user.setUserId("testId");
        user.setName("tester");
        user.setPassword("1234");

        //When
        userRepository.save(user);
        User expectedUser = userRepository.findById("testId");

        //Then
        softly.assertThat(user.getUserId()).isNotNull();
        softly.assertThat(user.getUserId()).isEqualTo(expectedUser.getUserId());
        softly.assertAll();
    }
}
