package codesquad.codestagram.login.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.login.service.LoginService;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class LoginController {

    private static final String SESSION_USER_KEY = "user";
    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String loginForm(){
        return "user/login";
    }


    @PostMapping("/login")
    public String login(
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session
    ){
        User user = loginService.validateLogin(userId, password);
        if(user == null){
            return "user/login_failed";
        }
        //세션에 SESSION_USER_KEY 라는 이름으로 데이터를 저장
        session.setAttribute(SESSION_USER_KEY,user);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam String userId,  // @PathVariable → @RequestParam
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String email,
                             HttpSession session) throws Exception {

        //세션에서 로그인 된 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute(SESSION_USER_KEY); //getAttribute가 Object 객체를 반환하기 때문에 User로 캐스팅 해줘야함

        boolean updatePossible = loginService.validateUserOwnership(loginUser, userId); //

        if(updatePossible){
            loginService.updateUserInfo(loginUser, password, name, email, session);
            return "redirect:/";
        }else{
            return "redirect:/auth/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        //세션 제거
        session.invalidate();
        return "redirect:/";
    }

}
