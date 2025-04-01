package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.ResourceNotFoundException;
import codesquad.codestagram.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;


import java.util.Optional;

@Controller
public class UserController {
    private UserService userService;
    private static final Logger log  = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/create")
    public String create(@ModelAttribute UserForm form, RedirectAttributes redirectAttributes) {
        User user = User.form(form);

        if (!userService.join(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ 이미 존재하는 아이디입니다.");
            return "redirect:/users/new"; // form.html로 다시
        }

        return "redirect:/users/list";
    }

    // UserController.java
    @GetMapping("/users/login")
    public String loginForm() {
        return "user/login"; // login.html 뷰 템플릿을 반환
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/";   // 메인 페이지로 리다이렉트
    }


    @PostMapping("/users/login")
    public String login(@RequestParam("loginId") String loginId, @RequestParam("password") String password, HttpSession session) {
        Optional<User> user = userService.findByLoginId(loginId);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get());
            return "redirect:/";
        } else {
            return "redirect:/users/login_failed";
        }

    }


    @GetMapping("/users/list")
    public String userlist(Model model) {
        model.addAttribute("users", userService.findUsers());
        return "user/list";
    }

    // 프로필 조회
    @GetMapping("/users/profile")
    public String profile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("user");
        // isAuthorized 내부에서 null 검증을 하므로 여기서는 바로 targetUserId로 검증 가능
        //로그인한 사용자가 있다면 그 사용자의 id를, 없다면 null을 전달
        if (!isAuthorized(session, sessionUser == null ? null : sessionUser.getId(), redirectAttributes)) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", sessionUser);
        return "user/profile";
    }


    //회원 정보 수정 폼 띄우기
    @GetMapping("/users/edit/{id}")
    public String updateForm(@PathVariable("id") Long id, HttpSession session,Model model, RedirectAttributes redirectAttributes) {
        if (!isAuthorized(session, id, redirectAttributes)) {
            return "redirect:/users/login";
        }
        User user = userService.findOne(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 사용자가 존재하지 않습니다."));

        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/users/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("currentPassword") String currentPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAuthorized(session, id, redirectAttributes)) {
            return "redirect:/users/login";
        }

        boolean result = userService.updateUser(id,currentPassword, newPassword, name,email);

        if (!result) {
            log.info("redirecting to /users/edit/{}?error=invalidPassword", id);
            redirectAttributes.addFlashAttribute("errorMessage", "❗ 비밀번호가 일치하지 않습니다."); //화면으로 메시지 전달
            return "redirect:/users/edit/" + id;
        }
        return "redirect:/users/profile";

    }

    @GetMapping("/users/{loginId}")
    public String viewUserProfile(@PathVariable("loginId") String loginId, Model model) {
        User user = userService.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 사용자가 존재하지 않습니다. loginId=" + loginId));
        model.addAttribute("user", user);
        return "user/profile";
    }

    //인증/인가 로직인 웹 계층(controller)에서 처리하는 것이 좋음
    private boolean isAuthorized(HttpSession session, Long targetUserId, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return false;
        }
        if (!sessionUser.getId().equals(targetUserId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "다른 사용자의 정보를 수정할 수 없습니다.");
            return false;
        }
        return true;
    }

}