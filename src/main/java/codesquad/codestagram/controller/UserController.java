package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.repository.UserRepository;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//UserController를 추가하고 @Controller 애노테이션 추가
@Controller
@RequestMapping("/users")
public class UserController { //url을 읽어서 처리

    private final UserService userService;

    private final MemoryUserRepository memoryUserRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, MemoryUserRepository memoryUserRepository) {
        this.userService = userService;
        this.memoryUserRepository = memoryUserRepository;
    }

    // 회원가입 폼 페이지 반환
    @GetMapping("/form")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    @PostMapping("/create")
    public String joinUser(@ModelAttribute User user){
        userService.join(user);
        return "redirect:/users"; //템플릿 엔진을 호출
    }
    // 회원 목록 조회
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = memoryUserRepository.findAll();
        model.addAttribute("users", users);
        return "user/list";  // user/list.html 렌더링
    }

    //회원 프로필 정보보기
    @GetMapping("/{userId}")
    public String showUserProfile(@PathVariable String userId, Model model){
    Optional<User> user = memoryUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile";  // profile.html 렌더링
        } else {
            return "error/404";  // 유저가 없을 경우 404 페이지로 이동
        }
    }

}
