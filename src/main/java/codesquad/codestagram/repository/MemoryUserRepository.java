/*
package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryUserRepository implements UserRepository{

    private static final ArrayList<User> store = new ArrayList<>();

    @Override
    public User join(User user) {
        store.add(user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        for (User user : store) {
            if (user.getUserId().equals(userId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return store;
    }

    @Override
    public void save(User updatingUser) {
        for(User user : store){
            if(user.getUserId().equals(updatingUser.getUserId())){
                user.setName(updatingUser.getName());
                user.setEmail(updatingUser.getEmail());
                user.setPassword(updatingUser.getPassword());
            }
        }
    }

}
*/