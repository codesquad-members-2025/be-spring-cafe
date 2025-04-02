package codesquad.codestagram.controller;

import codesquad.codestagram.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Optional.get() 실패 등
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, RedirectAttributes redirectAttributes) {
        log.warn("[no-found.html] {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", "요청하신 정보를 찾을 수 없습니다.");
        return "error/no-found";
    }

    // User 를 찾을 수 없을 때
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        log.warn("[UserNotFount] {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "error/user-not-found";
    }

    // 예상치 못한 null
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException ex, RedirectAttributes redirectAttributes) {
        log.error("[NullPointer] {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("error", "처리 중 문제가 발생했습니다.");
        return "error/null-pointer";
    }

    // 기타 모든 예외
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("[Unhandled Exception] {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("error", "알 수 없는 오류가 발생했습니다.");
        return "error/general-error";
    }
}
