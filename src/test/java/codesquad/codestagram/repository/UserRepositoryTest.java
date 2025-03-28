package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private SoftAssertions softly;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        this.softly = new SoftAssertions();
    }

    @Test
    @DisplayName("testId라는 아이디를 가진 회원이 생성된다.")
    void save() {
        //Given
        User user = new User();
        user.setLoginId("testId");
        user.setName("tester");
        user.setPassword("1234");

        //When
        User actualUser = userRepository.save(user);
        User expectedUser = userRepository.findById(actualUser.getId()).orElseThrow();

        //Then
        softly.assertThat(user.getLoginId()).isNotNull();
        softly.assertThat(user.getLoginId()).isEqualTo(expectedUser.getLoginId());
        softly.assertAll();
    }

    @Test
    @DisplayName("저장된 회원은 두명이다.")
    void findById() {
        //Given
        User user1 = new User();
        user1.setLoginId("testId1");
        user1.setName("tester2");
        user1.setPassword("1234");
        userRepository.save(user1);

        User user2 = new User();
        user2.setLoginId("testId2");
        user2.setName("tester2");
        user2.setPassword("1234");
        userRepository.save(user2);

        //When
        List<User> users = userRepository.findAll();

        //Then
        softly.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("닉네임이 머드로 변경된다.")
    void update() {
        //Given
        User user = new User();
        user.setLoginId("testerId");
        user.setName("tester");
        user.setPassword("1234");
        User actualUser = userRepository.save(user);

        //When
        actualUser.setName("머드");
        actualUser = userRepository.save(actualUser);

        //Then
        softly.assertThat(actualUser.getName()).isEqualTo("머드");
    }
}
