package codesquad.codestagram.user.service;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.impl.MemoryUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserService: 회원 관리 기능 테스트")
class UserServiceTest {

    UserService userService;
    MemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new MemoryUserRepository();
        this.userService = new UserService(repository);
    }

    @AfterEach
    void tearDown() {
        repository.clearStore();
    }

    @Test
    @DisplayName("사용자 등록 후 동일한 seq로 조회하면 등록된 사용자 정보를 반환해야 한다.")
    void join() {

        // given: 테스트용 User 객체 생성
        User user = new User(
                "id",
                "password",
                "name",
                "email"
        );
        user.setSeq(10000L);

        // when: User 객체 등록, 조회
        userService.join(user);
        User findUser = userService.findUser(user.getSeq());

        // then: 테스트용 User 객체와 조회된 객체는 같아야 한다.
        assertThat(findUser).isEqualTo(user);
    }


    @Test
    @DisplayName("저장된 모든 사용자 목록이 올바른 순서로 반환되어야 한다.")
    void findUsers() {
        // given: 테스트용 User 객체 4개 생성 후 저장
        User user1 = new User("id1", "password", "name", "email");
        User user2 = new User("id2", "password", "name", "email");
        User user3 = new User("id3", "password", "name", "email");
        User user4 = new User("id4", "password", "name", "email");
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        repository.save(user4);

        // when: 저장된 user 객체 리스트 조회
        List<User> users = userService.findUsers();

        // then: 조회된 User 리스트가 테스트용 User 리스트와 사이즈, 참조값이 동일해야 함
        assertThat(users).isEqualTo(new ArrayList<>(List.of(user1, user2, user3, user4)))
                .hasSize(4);
    }
}
