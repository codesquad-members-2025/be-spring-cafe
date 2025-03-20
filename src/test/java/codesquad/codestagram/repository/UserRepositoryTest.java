package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

public class UserRepositoryTest {
    UserRepository memoryUserRepository = new MemoryUserRepository();
    @AfterEach
    void tearDown() {
        memoryUserRepository.clearStore();
    }

    @Test
    @DisplayName("유저를 추가하고, 모든 유저를 가져올 수 있다.")
    void add_user_and_get_all_users() {
        User user1 = new User("jdragon","dino","1234","je@d");
        memoryUserRepository.save(user1);
        User user2 = new User("jdragon2","dino2","12342","je@d");
        memoryUserRepository.save(user2);
        List<User> allUsers = memoryUserRepository.fineAll();
        assertThat(allUsers.size()).isEqualTo(2);
        assertThat(allUsers.get(0)).isEqualTo(user1);
        assertThat(allUsers.get(1)).isEqualTo(user2);
    }

    @Test
    @DisplayName("유저를 loginId로 찾을 수 있다.")
    void find_user_by_loginId() {
        User user1 = new User("jdragon","dino","1234","je@d");
        memoryUserRepository.save(user1);
        User user2 = new User("jdragon2","dino2","12342","je@d");
        memoryUserRepository.save(user2);
        Optional<User> findUser = memoryUserRepository.fineByUserId("jdragon");
        Optional<User> noUser = memoryUserRepository.fineByUserId("jdragon55");
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getUserId()).isEqualTo("jdragon");
        assertThat(noUser.isPresent()).isFalse();
    }

    @Test
    @DisplayName("유저를 이름으로 찾을 수 있다.")
    void find_user_by_name() {
        User user1 = new User("jdragon","dino","1234","je@d");
        memoryUserRepository.save(user1);
        User user2 = new User("jdragon2","dino2","12342","je@d");
        memoryUserRepository.save(user2);
        Optional<User> findUser = memoryUserRepository.fineByName("dino");
        Optional<User> noUser = memoryUserRepository.fineByName("dino55");
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getName()).isEqualTo("dino");
        assertThat(noUser.isPresent()).isFalse();
    }
}
