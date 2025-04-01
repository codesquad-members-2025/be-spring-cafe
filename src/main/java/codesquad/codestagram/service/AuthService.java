package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;

import codesquad.codestagram.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean checkLogin(HttpSession session) {
        User sessionedUser = (User) session.getAttribute(SESSIONED_USER);
        return sessionedUser == null;
    }
}
