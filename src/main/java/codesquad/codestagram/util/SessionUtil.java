package codesquad.codestagram.util;


import codesquad.codestagram.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    public static final String SESSION_USER_KEY = "user";

    //로그인 여부를 확인
    public static boolean isUserLoggedIn(HttpSession session){
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    //현재 로그인한 사용자 가져오기
    public static User getLoggedInUser(HttpSession session){
        return (User) session.getAttribute(SESSION_USER_KEY);
    }
}
