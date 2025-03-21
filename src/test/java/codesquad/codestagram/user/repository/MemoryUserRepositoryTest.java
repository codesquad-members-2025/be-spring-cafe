package codesquad.codestagram.user.repository;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.impl.MemoryUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@DisplayName("MemoryUserRepository: 회원 관리 기능 테스트")
class MemoryUserRepositoryTest {

    MemoryUserRepository repository = new MemoryUserRepository();

    @AfterEach
    void tearDown() {
        repository.clearStore();
    }

    @Test
    @DisplayName("새로운 User 객체가 저장되면 동일한 객체를 조회할 수 있어야 한다.")
    void save() {
        // given: 테스트용 User 객체 생성
        User user = new User(
                "id",
                "password",
                "name",
                "email"
        );
        // when: User 객체 저장
        repository.save(user);

        // then: 저장된 객체와 조회된 객체가 동일해야 함.
        User findUser = repository.findBySeq(user.getSeq()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("저장된 모든 사용자 목록을 올바르게 반환해야 한다.")
    void findAll() {
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
        List<User> result = repository.findAll();

        // then: 조회된 User 리스트가 테스트용 User 리스트와 사이즈, 참조값이 동일해야 함
        assertThat(result).isEqualTo(new ArrayList<>(List.of(user1, user2, user3, user4)))
                .hasSize(4);
    }
}
