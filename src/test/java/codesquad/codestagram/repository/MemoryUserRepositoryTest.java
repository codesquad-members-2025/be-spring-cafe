package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryUserRepositoryTest {
    MemoryUserRepository repository = new MemoryUserRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        //given
        User user = new User();
        user.setName("gyuwon");
        //when
        repository.save(user);
        //then
        User result = repository.findById(user.getId()).get();
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void findByName() {
        //given
        User user1 = new User();
        user1.setName("gyuwon");
        repository.save(user1);

        User user2 = new User();
        user2.setName("brie");
        repository.save(user2);

        //when
        User result = repository.findByName("gyuwon").get();

        //then
        assertThat(result).isEqualTo(user1);
    }

    @Test
    public void findByAll() {
        User user1 = new User();
        user1.setName("gyuwon");
        repository.save(user1);

        User user2 = new User();
        user2.setName("brie");
        repository.save(user2);

        //when
        List<User> result = repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}
