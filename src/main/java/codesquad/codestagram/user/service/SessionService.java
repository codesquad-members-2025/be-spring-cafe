package codesquad.codestagram.user.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    private static final String USER_ID_SESSION_KEY = "LOGGED_IN_USER_ID";
    private static final String REDIRECT_URL_SESSION_KEY = "REDIRECT_AFTER_LOGIN_URL";

    public void login(HttpSession session, Long userId) {
        session.setAttribute(USER_ID_SESSION_KEY, userId);
    }

    public void logout(HttpSession session) {
        // 로그아웃할 때 remove말고 invalide 하자
//        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    public Optional<Long> getLoggedInUserIdOpt(HttpSession session) {
        Object userId = session.getAttribute(USER_ID_SESSION_KEY);
        if (userId instanceof Long) {
            return Optional.of((Long) userId);
        }
        return Optional.empty();
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_ID_SESSION_KEY) != null;
    }

    public void saveRedirectUrl(HttpSession session, String url) {
        session.setAttribute(REDIRECT_URL_SESSION_KEY, url);
    }

    public String popRedirectUrl(HttpSession session) {
        Object redirectUrl = session.getAttribute(REDIRECT_URL_SESSION_KEY);
        session.removeAttribute(REDIRECT_URL_SESSION_KEY);

        return (redirectUrl instanceof String s) ? s : "/";
    }
}
