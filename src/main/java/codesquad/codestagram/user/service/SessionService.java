package codesquad.codestagram.user.service;

import codesquad.codestagram.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

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

    public Long getLoggedInUserId(HttpSession session) {
        return (Long) session.getAttribute(USER_ID_SESSION_KEY);
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_ID_SESSION_KEY) != null;
    }
}
