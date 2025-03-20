package codesquad.codestagram.controller;

import codesquad.codestagram.Entity.User;
import codesquad.codestagram.dto.BoardRequestDto;
import codesquad.codestagram.dto.BoardResponseDto;
import codesquad.codestagram.dto.CommentResponseDto;
import codesquad.codestagram.dto.UserResponseDto;
import codesquad.codestagram.service.BoardService;
import codesquad.codestagram.service.CommentService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;

    BoardController(BoardService boardService, UserService userService, CommentService commentService) {
        this.boardService = boardService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        List<BoardResponseDto> boards = boardService.getAllPosts();
        model.addAttribute("boards", boards);

        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("userName", loginUser);
        }
        return "user/index";
    }

    @GetMapping("/boards/{id}")
    public String getBoard(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인 하세용");
            return "redirect:/";
        }

        List<CommentResponseDto> comments = commentService.getCommentsByBoardId(id);
        BoardResponseDto board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("loginUser", loginUser);
        return "qna/show";
    }

    @GetMapping("/questions")
    public String questions(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인 하세용");
            return "redirect:/";
        }
        model.addAttribute("writerName", loginUser);
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

    @GetMapping("/boards/{id}/revise")
    public String showEditBoardForm(@PathVariable Long id, Model model, HttpSession session) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/";
        }
        BoardResponseDto board = boardService.getBoardById(id);
        if (!board.getWriterId().equals(loginUser.getId())) {
            return "redirect:/boards/" + id + "?error=unauthorized";
        }
        model.addAttribute("writerName", loginUser.getName());
        model.addAttribute("board", board);
        return "qna/change";
    }

    @PutMapping("/boards/{id}/revise")
    public String updateBoard(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content
    ){
        boardService.updateBoard(id, title, content);
        return "redirect:/boards/" + id;
    }

    @DeleteMapping("/boards/{id}")
    public String deleteBoard(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인 하세용");
            return "redirect:/";
        }

        boolean isDeleted = boardService.deleteBoard(id, loginUser.getUserSeq());

        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("deleteError", "삭제 권한이 없습니다.");
            return "redirect:/boards/" + id;
        }

        return "redirect:/";
    }

    @PostMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long boardId) {
        commentService.deleteComment(id);
        return "redirect:/boards/" + boardId;
    }
}