package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/create") //이거 강의에서는 new 로 하는데.. 구분해줘야 하나?
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
    public String userProfile(@PathVariable("userId") String userId, Model model) {
        Optional<User> user = userService.findByUserId(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile"; //사용자 있으면 프로필로 이동
        } else {
            System.out.println("사용자 없음: " + userId); // 로그 추가
            return "redirect:/user/list"; //사용자가 없으면 목록으로 리디렉션
        }
    }
}
