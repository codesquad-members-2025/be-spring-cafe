package codesquad.codestagram.user.service;

import codesquad.codestagram.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    private static final String USER_ID_SESSION_KEY = "LOGGED_IN_USER_ID";

    public void login(HttpSession session, Long userId) {
        session.setAttribute(USER_ID_SESSION_KEY, userId);
    }

    public void logout(HttpSession session) {
        // 로그아웃할 때 remove말고 invalide 하자
//        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    public Optional<Long> getLoggedInUserId(HttpSession session) {
        Object userId = session.getAttribute(USER_ID_SESSION_KEY);
        if (userId instanceof Long) {
            return Optional.of((Long) userId);
        }
        return Optional.empty();
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_ID_SESSION_KEY) != null;
    }
}
