package codesquad.codestagram.util;

import codesquad.codestagram.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AuthUtil {
    //인증(로그인여부만 확인)
    public static boolean isLogined(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) { //세션에 user 속성이 있는지만 체크 -> 변수로 할당할 필요 없음
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return false;
        }
        return true;
    }

    //인가(동알 사용자인지(권한 확인)) -> 이 전에 로그인 상태인지 반드시 확인해야함
    public static boolean isOwner(HttpSession session, Long targetUserId, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("user");
        if (!sessionUser.getId().equals(targetUserId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "다른 사용자의 정보를 수정할 수 없습니다.");
            return false;
        }
        return true;
    }

    //한번에 확인
    public static boolean isAuthorized(HttpSession session, Long targetUserId, RedirectAttributes redirectAttributes) {
        return isLogined(session, redirectAttributes) && isOwner(session, targetUserId, redirectAttributes);
    }
}