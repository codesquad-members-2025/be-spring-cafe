package codesquad.codestagram.service;

import codesquad.codestagram.Entity.User;
import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.repository.UserMapRepository;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapRepository userMapRepository;

    public UserService(@Qualifier("userJpaRepository") UserRepository userRepository,
                       @Qualifier("userMapRepository") UserMapRepository userMapRepository) {
        this.userRepository = userRepository;
        this.userMapRepository = userMapRepository;
    }

    public UserResponseDto registerUser(UserRequestDto dto) {
        User user = new User(dto.getId(), dto.getName(), dto.getPassword(), dto.getEmail());

        User saveUser = userRepository.save(user);
        userMapRepository.save(saveUser);

        return new UserResponseDto(saveUser.getUserSeq(), saveUser.getId(), saveUser.getName(), saveUser.getEmail());
    }

    public ArrayList<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        ArrayList<UserResponseDto> users = new ArrayList<>();
        for (User user : userList) {
            users.add(new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail()));
        }
        return users;
    }

    public UserResponseDto getUserById(String id) {
        User user = userMapRepository.findByUserId(id);
        if (user != null) {
            return new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
        }
        return null;
    }

    public User getUserBySeq(Long seq) {
        User user = userMapRepository.findByUserSeq(seq);
        return user;
    }

    public UserResponseDto authenticate(String id, String password) {
        User user = userRepository.findById(id);

        if (user == null) {
            user = userMapRepository.findByUserId(id);
        }

        if (user != null && user.getPassword().equals(password)) {
            return new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
        }
        return null;
    }

    public void updateUser(String id, String name, String email) {
        User user = userRepository.findById(id);

        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
        }

        User mapUser = userMapRepository.findByUserId(id);
        if (mapUser != null) {
            mapUser.setName(name);
            mapUser.setEmail(email);
        }
    }

}