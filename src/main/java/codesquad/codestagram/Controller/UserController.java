package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
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
        User user = new User(); //Entity 생성
        user.setLoginId(form.getLoginId()); //DTO -> Entity 변환
        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setEmail(form.getEmail());

        boolean success = userService.join(user);

        if (!success) {
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

    @GetMapping("/users/{loginId}")
    public String userProfile(@PathVariable("loginId") String loginId, Model model, RedirectAttributes redirectAttributes) { //RedirectAttributes: 리다이렉트할 때 데이터를 잠깐 보낼 수 있게 도와주는 객체(팝업에 들어갈 메시지를 전달하는 통로)
        Optional<User> user = userService.findByLoginId(loginId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile"; //사용자 있으면 프로필로 이동
        } else {
            log.warn("사용자 없음: {}", loginId); // 로그 추가
            redirectAttributes.addFlashAttribute("errorMessage", "존재하지 않는 사용자입니다."); //에러 메시지 사용자에게 보여줌.
            return "redirect:/users/list";
        }
    }

    //회원 정보 수정 폼 띄우기
    @GetMapping("/users/edit/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.findOne(id);

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user/updateForm"; // 수정 폼 화면 반환
        } else {
            log.warn("수정하려는 사용자 없음: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "존재하지 않는 사용자입니다.");
            return "redirect:/users/list";
        }
    }

    @PutMapping("/users/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("currentPassword") String currentPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email,
                             RedirectAttributes redirectAttributes) {

        boolean result = userService.updateUser(id,currentPassword, newPassword, name,email);

        if (!result) {
            log.info("redirecting to /users/edit/{}?error=invalidPassword", id);
            redirectAttributes.addFlashAttribute("errorMessage", "❗ 비밀번호가 일치하지 않습니다."); //화면으로 메시지 전달
            return "redirect:/users/edit/" + id;
        }

        return "redirect:/users/list";

    }

}
