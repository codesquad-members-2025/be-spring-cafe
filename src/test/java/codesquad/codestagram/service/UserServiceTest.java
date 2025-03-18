package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

public class UserServiceTest {
    UserRepository memoryUserRepository = new MemoryUserRepository();
    UserService userService = new UserService();

    @AfterEach
    void tearDown() {
        memoryUserRepository.clearStore();
    }

    @Test
    @DisplayName("join해서 repository에 추가할 수 있다.")
    void add_user() {
        User user = new User("jdragon","dino","1234","aa@aa");
        userService.join(user);
        List<User> allUsers = userService.findAllUsers();
        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.get(0)).isEqualTo(user);
    }

    @Test
    @DisplayName("만약 동일한 아이디가 이미 존재하면, 에러를 던진다.")
    void add_same_id_user() {
        User user = new User("jdragon","dino","1234","aa@aa");
        userService.join(user);
        User user2 = new User("jdragon","dino1","12341","aa@aa");
        assertThatThrownBy(()->userService.join(user2)).isInstanceOf(IllegalStateException.class).hasMessage("이미 존재하는 아이디입니다.");
    }
}

