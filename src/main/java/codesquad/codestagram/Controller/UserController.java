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
        return "redirect:/users/list";
    }

    @GetMapping("/users/list")
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
            return "redirect:/users/list"; //사용자가 없으면 리다이렉션 하는 것보다 사용자에게 에러 메시지를 보여 주는게 더 좋은 방법일까?
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
                             @RequestParam("email") String email) {
        boolean result = userService.updateUser(id,currentPassword, newPassword, name,email);

        if (!result) {
            //비밀번호 틀린 경우 or 유저 없는 경우
            log.info("redirecting to /users/edit/{}?error=invalidPassword", id);
            return "redirect:/users/edit/" + id + "?error=invalidPassword"; //에러 메시지 전달
        }

        return "redirect:/users/list";

    }

}
