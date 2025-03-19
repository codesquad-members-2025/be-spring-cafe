package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("userMapRepository")
public class UserMapRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long userSeq = 1L;

    public User save(User user) {
        userMap.put(userSeq, user);
        userSeq++;
        return user;
    }

    public User findByUserId(String id) {
        for (User user : userMap.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        return new ArrayList<>(userMap.values());
    }

    public void deleteByUserId(String id) {
        Iterator<User> iterator = userMap.values().iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
    }
}
