package codesquad.codestagram.login.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //로그인 검증
    public User validateLogin(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        //1. 피드백 : equals -> matchPassword 메서드로 구현
        if(user != null && user.matchPassword(password)){
            return user;
        }
        return null;
    }

    //validateUpdate -> validateUserOwnership로 메서드 이름 fix
    // loginUser가 본인의 정보에 대한 권한을 가지고 있는지 검증한다
    public boolean validateUserOwnership(User loginUser, String userId) {
        return loginUser != null && loginUser.getUserId().equals(userId);
    }

    //회원 정보 수정 -> @Transactional 제거
    public void updateUserInfo(User loginUser, String password, String name, String email, HttpSession session) throws Exception {
        if(loginUser.getPassword().equals(password)){
            loginUser.setName(name);
            loginUser.setEmail(email);
            session.setAttribute("user",loginUser);
            userRepository.save(loginUser);
        }else{
            //예외처리
            throw new Exception("비밀번호가 일치해야 수정이 가능합니다");
        }
    }
}
