package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String create(@ModelAttribute UserForm form) {
        User user = new User();
        user.setUserId(form.getUserId());
        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setEmail(form.getEmail());

        userService.join(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String userlist(Model model) {
        model.addAttribute("users", userService.findUsers());
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String userProfile(@PathVariable("userId") String userId, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.findByUserId(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile"; //사용자 있으면 프로필로 이동
        } else {
            log.warn("사용자 없음: {}", userId); // 로그 추가
            redirectAttributes.addFlashAttribute("errorMessage", "존재하지 않는 사용자입니다.");
            return "redirect:/user/list"; //사용자가 없으면 리다이렉션 하는 것보다 사용자에게 에러 메시지를 보여 주는게 더 좋은 방법일까?
        }
    }

    @GetMapping("/user/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam String currentPassword,
                             @RequestParam String newPassword,
                             @RequestParam String name,
                             @RequestParam String email) {
        boolean result = userService.updateUser(id,currentPassword, newPassword, name,email);

        if (!result) {
            //비밀번호 틀린 경우 or 유저 없는 경우
            log.info("redirecting to /user/edit/{}?error=invalidPassword", id);
            return "redirect:/user/edit/" + id + "?error=invalidPassword"; //에러 메시지 전달
        }

        return "redirect:/user/list";

    }

}
