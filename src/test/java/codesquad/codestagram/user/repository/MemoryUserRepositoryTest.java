package codesquad.codestagram.user.repository;

import codesquad.codestagram.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryUserRepositoryTest {

    MemoryUserRepository repository = new MemoryUserRepository();

    @AfterEach
    void tearDown() {
        repository.clearStore();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void save() {
        User user = new User(
                "id",
                "password",
                "name",
                "email"
        );
        repository.save(user);

        User findUser = repository.findBySeq(user.getSeq()).orElseThrow(
                () -> new IllegalStateException(user.getId() + "회원은 존재하지 않습니다.")
        );

        assertThat(user).isEqualTo(findUser);
    }

    @Test
    @DisplayName("회원 목록 조회 테스트")
    void findAll() {
        User user1 = new User("id1", "password", "name", "email");
        User user2 = new User("id2", "password", "name", "email");
        User user3 = new User("id3", "password", "name", "email");
        User user4 = new User("id4", "password", "name", "email");
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        repository.save(user4);

        List<User> result = repository.findAll();

        assertThat(result).isEqualTo(new ArrayList<>(List.of(user1, user2, user3, user4)))
                .hasSize(4);
    }
}