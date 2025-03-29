package codesquad.codestagram.common.interceptor;

import codesquad.codestagram.common.constants.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConstants.USER_SESSION_KEY) == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        return true;
    }

}
