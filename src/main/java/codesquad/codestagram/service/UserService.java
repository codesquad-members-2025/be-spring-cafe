package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long userSeq = 1L;

    public UserResponseDto registerUser(UserRequestDto dto) {
        User user = new User(userSeq, dto.getId(), dto.getName(), dto.getPassword(), dto.getEmail());
        userMap.put(userSeq, user);
        userSeq++;
        UserResponseDto responseDto = new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
        return responseDto;
    }

    public ArrayList<UserResponseDto> getAllUsers() {
        ArrayList<UserResponseDto> users = new ArrayList<>();
        for (User user : userMap.values()) {
            UserResponseDto dto = new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
            users.add(dto);
        }
        return users;
    }

    public UserResponseDto getUserById(String id) {
        for (User user : userMap.values()) {
            if (user.getId().equals(id)) {
                return new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
            }
        }
        return null;
    }

    public UserResponseDto authenticate(String id, String password) {
        for (User user : userMap.values()) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return new UserResponseDto(user.getUserSeq(), user.getId(), user.getName(), user.getEmail());
            }
        }
        return null;
    }
}