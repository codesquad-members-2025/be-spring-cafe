package codesquad.codestagram.exception;

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

    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFound(BoardNotFoundException e, Model model, HttpServletResponse response) {
        response.setStatus(404);
        log.warn("Board not found", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, Model model, HttpServletResponse response) {
        response.setStatus(404);
        log.warn("User not found", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ReplyNotFoundException.class)
    public String handleReplyNotFound(ReplyNotFoundException e, Model model, HttpServletResponse response) {
        response.setStatus(404);
        log.warn("Reply not found", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    /**
     * 잘못된 인자 등 일반적인 클라이언트 오류, (요청의 "값"이 잘못된 경우에 많이 씀)
     * 예를 들어 (ID는 1 이상이어야 합니다.)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model, HttpServletResponse response) {
        response.setStatus(400); // 보통 400 Bad Request로 처리
        log.error("잘못된 요청", e);
        model.addAttribute("errorMessage", "잘못된 요청입니다.");
        return "error/400";
    }

    /**
     * 정적 리소스가 존재하지 않는 경우 (이미지 등)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleStaticResourceError(NoResourceFoundException e, Model model, HttpServletResponse response) {
        response.setStatus(404);
        log.error("정적 리소스를 찾을 수 없음", e);
        model.addAttribute("errorMessage", "요청하신 리소스를 찾을 수 없습니다.");
        return "error/404";
    }


    /**
     * 그 외 모든 서버 오류
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception e, Model model, HttpServletResponse response) {
        response.setStatus(500); // 서버 내부 오류
        log.error("서버 오류 발생", e);
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        return "error/500";
    }
}