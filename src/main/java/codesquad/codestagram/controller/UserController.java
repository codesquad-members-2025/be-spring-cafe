package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//UserController를 추가하고 @Controller 애노테이션 추가
@Controller
@RequestMapping("/users")
public class UserController { //url을 읽어서 처리

    private final UserService userService;


    //생성자를 자동으로 주입
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 폼 페이지 렌더링
    @GetMapping("/form")
    public String showSignupForm() {
        return "user/form";
    }

    @PostMapping("/create")
    public String joinUser(@ModelAttribute User user){ // HTML 폼 데이터를 User객체로 자동 매핑
        userService.join(user);
        return "redirect:/users";
    }
    // 회원 목록 조회
    @GetMapping
    public String listUsers(Model model) {

        List<User> users = userService.getAllUsers();
        //회원 리스트를 뷰에 전달
        model.addAttribute("users", users);
        return "user/list";  // user/list.html 렌더링
    }

    //회원 프로필 정보보기
    @GetMapping("/{userId}")
    public String showUserProfile(@PathVariable String userId, Model model){
        User user = userService.findOne(userId);

       if (user!= null) {
            model.addAttribute("user", user);
            return "user/profile";  // profile.html로 렌더링
        } else {
            return "error/404";  // 유저가 없을 경우 404 페이지로 이동
       }
    }

    //회원정보 수정 화면
    @GetMapping("/{userId}/form")
    public String getUserInformation(@PathVariable String userId, Model model){
       // Optional<User> user = userService.findByUserId(userId);
        User user = userService.findOne(userId);

        if (user!=null) {
            model.addAttribute("user", user);
            return "user/updateForm";  // updateForm.html로 렌더링
        } else {
            return "error/404";  // 유저가 없을 경우 404 페이지로 이동
        }
    }

    //회원정보 수정
    @PutMapping("{userId}/update")
    public String updateUserInformation(@PathVariable String userId, @ModelAttribute User updatingUser){
        userService.updateUser(userId, updatingUser);
        return "redirect:/users";
    }

}
