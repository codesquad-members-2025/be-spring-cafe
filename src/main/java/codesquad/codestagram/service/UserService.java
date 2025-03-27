package codesquad.codestagram.service;

import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initExampleUsers(){
        userRepository.save(new User("a","a","a@a","bazzi"));
        userRepository.save(new User("b","b","b@b","bazzi1"));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void join(UserRequestDto dto){
        userRepository.save(dto.toEntity());
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    public User login(String userid, String password){
        Optional<User> findUser = userRepository.findByUserid(userid);
        if(findUser.isPresent()){
            User user = findUser.get();
            if(user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
        //user service findByid = 회원가입한 아이디 찾아오는거임 => 예외처리를 해야하는 상황과 널을 처리하는 상황
    }
}
