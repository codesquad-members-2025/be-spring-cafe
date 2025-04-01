package codesquad.codestagram.controller;

import codesquad.codestagram.dto.LoginForm;
import codesquad.codestagram.service.LoginService;
import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/users/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session){

        if(bindingResult.hasErrors()){
            return "user/login";
        }

        User user = loginService.login(loginForm.getUserId(), loginForm.getPassword());

        if(user == null){
            bindingResult.reject("login Fail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "user/login";
        }

        session.setAttribute(SessionConst.LOGIN_USER, user);


        return "redirect:/";
    }

    @PostMapping("users/logout")
    public String logout(HttpSession session){
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }



}
