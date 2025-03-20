package codesquad.codestagram.controller;

import codesquad.codestagram.Entity.User;
import codesquad.codestagram.dto.BoardRequestDto;
import codesquad.codestagram.dto.BoardResponseDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.service.BoardService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<BoardResponseDto> boards = boardService.getAllPosts();
        model.addAttribute("boards", boards);
        return "user/index";
    }

    @GetMapping("/boards/{id}")
    public String getBoard(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인 하세용");
            return "redirect:/";
        }

        BoardResponseDto board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "qna/show";
    }

    @GetMapping("/questions")
    public String questions(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인 하세용");
            return "redirect:/";
        }
        model.addAttribute("writerName", loginUser.getName());
        return "qna/form";
    }

    @PostMapping("/questions")
    public String postQuestions(@ModelAttribute BoardRequestDto dto, HttpSession session) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        User user = userService.getUserBySeq(loginUser.getUserSeq());
        boardService.createBoard(dto, user);
        return "redirect:/";
    }
}