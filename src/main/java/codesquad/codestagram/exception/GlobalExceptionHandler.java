package codesquad.codestagram.exception;

import codesquad.codestagram.Controller.UserController;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log  = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleNotFound(IllegalArgumentException e, Model model, HttpServletResponse response) {
        response.setStatus(404); // 404 에러
        log.error("오류 발생", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404"; // templates/error/404.html 렌더링
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleStaticResourceError(NoResourceFoundException e, Model model, HttpServletResponse response) {
        response.setStatus(404);
        log.error("오류 발생", e);
        model.addAttribute("errorMessage", "요청하신 리소스를 찾을 수 없습니다.");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception e, Model model, HttpServletResponse response) {
        response.setStatus(500); // 500 에러
        log.error("오류 발생", e);
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        return "error/500";
    }
}