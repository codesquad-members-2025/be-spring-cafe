package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());
    private static Long sequence = 0L;

    public User save(User user) {
        user.setId(++sequence);
        users.add(user);
        return user;
    }
}
