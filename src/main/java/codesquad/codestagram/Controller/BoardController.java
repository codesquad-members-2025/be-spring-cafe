package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class BoardController {
    private BoardService boardService;

    @Autowired // 생성자가 1개면 spring4.3 이상에서는 자동 주입됨. 하지만 명시적으로 적어주기 잘 모르니까...
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //게시판 폼으로 이동
    @GetMapping("/boards/new")
    public String newBoardForm() {
        return "qna/form";
    }

    @PostMapping("/boards/create")
    public String createBoard(@ModelAttribute BoardForm form) {
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());

        boardService.writeBoard(board);
        return "redirect:/"; //메인 페이지로 이동

    }

    @GetMapping("/")
    public String listBoards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "qna/list"; // 게시글 목록 화면
    }

    /**
     * 게시글 상세보기
     * GET 요청: /boards/{boardId}
     */
    @GetMapping("/boards/{boardId}")
    public String showBoard(@PathVariable Long boardId, Model model) {
        boardService.getBoardBydId(boardId)
                .ifPresent(board -> model.addAttribute("board", board));
        return "qna/show"; // 게시글 상세 화면 (qna/show.html)
    }
}
