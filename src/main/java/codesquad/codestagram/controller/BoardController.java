package codesquad.codestagram.controller;

import codesquad.codestagram.dto.BoardRequestDto;
import codesquad.codestagram.dto.BoardResponseDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BoardController {
    private final BoardService boardService;

    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<BoardResponseDto> boards = boardService.getAllPosts();
        model.addAttribute("boards", boards);
        return "user/index";
    }

    @GetMapping("/boards/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "qna/show";
    }

    @GetMapping("/questions")
    public String questions(Model model, HttpSession session) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        String writerName = (loginUser != null) ? loginUser.getName() : "익명";
        model.addAttribute("writerName", writerName);
        return "qna/form";
    }

    @PostMapping("/questions")
    public String postQuestions(@ModelAttribute BoardRequestDto dto, HttpSession session) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        String writer = (loginUser != null) ? loginUser.getName() : "익명";
        boardService.createBoard(dto, writer);
        return "redirect:/";
    }


}