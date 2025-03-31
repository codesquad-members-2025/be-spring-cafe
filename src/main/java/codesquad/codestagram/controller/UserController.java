package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public String create(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            HttpSession session) {
        User user = new User(userId, name, password, email);
        userService.join(user);
        session.setAttribute("loginUser", user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String list(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/user/{userId}")
    public String profile(@PathVariable("userId") String userId, Model model) {
        User user = userService.findOneUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model) {
        User user = userService.findOneUser(userId).get();
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/users/{userId}/update")
    public String update(
            @PathVariable String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("loginUser");
        if (!password.equals(sessionUser.getPassword())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("user", sessionUser);
            return "user/updateForm";
        }

        sessionUser.setName(name);
        sessionUser.setEmail(email);

        userService.update(sessionUser);
        return "redirect:/users";
    }

    @GetMapping("/user/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session) {
        Optional<User> user = userService.findOneUser(userId);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("loginUser", user.get());  // 세션에 로그인 사용자 저장, setAttribute 는 세션에 데이터 저장할 때 사용
            return "redirect:/";
        }
        return "user/login_failed";  // 로그인 실패 시 어떤 걸 리턴할 것인가?
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
