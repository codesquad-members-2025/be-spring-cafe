package codesquad.codestagram.Controller;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.BoardForm;
import codesquad.codestagram.service.BoardService;
import codesquad.codestagram.util.AuthUtil;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

@Controller
public class BoardController {
    private BoardService boardService;
    private static final Logger log  = LoggerFactory.getLogger(UserController.class);

    @Autowired // 생성자가 1개면 spring4.3 이상에서는 자동 주입됨. 하지만 명시적으로 적어주기 잘 모르니까...
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/")
    public String listBoards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        log.info("현재 저장된 게시글 개수: {}",  boards.size());
        model.addAttribute("boards", boards);
        return "index"; // 게시글 목록 화면
    }

    /**
     * 게시글 상세보기
     * GET 요청: /boards/{boardId}
     */
    @GetMapping("/boards/{boardId}")
    public String showBoard(@PathVariable("boardId") Long boardId, Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }
        Board board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        model.addAttribute("board", board);
        return "qna/show"; // 게시글 상세 화면 (qna/show.html)
    }
    //글 작성 화면
    @GetMapping("/boards/new")
    public String newBoardForm(HttpSession session,  RedirectAttributes redirectAttributes) {
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }
        return "qna/form";
    }

    @PostMapping("/boards/create")
    public String createBoard(@ModelAttribute BoardForm form,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!AuthUtil.isLogined(session, redirectAttributes)) {
            return "redirect:/users/login";
        }

        User user = (User) session.getAttribute("user");
        Board board = Board.form(form);
        board.setWriter(user);

        log.info("게시글 생성 요청: 제목={}, 작성자id={}", board.getTitle(), board.getWriter().getLoginId());
        boardService.writeBoard(board);
        return "redirect:/"; //메인 페이지로 이동

    }

    //게시글 수정 폼
    @GetMapping("/boards/{boardId}/edit")
    public String editBoardForm(@PathVariable("boardId") Long boardId, Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Board board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!AuthUtil.isAuthorized(session, board.getWriter().getId(), redirectAttributes)) {
            return "redirect:/boards/" + boardId; //todo 오류 메시지 수정하기.
        }
        model.addAttribute("board", board);
        return "qna/edit";
    }

    //게시글 수정
    @PutMapping("/boards/{boardId}/edit")
    public String updateBoard(@PathVariable("boardId") Long boardId,
                              @ModelAttribute BoardForm form,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Board board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        //로그인 여부 & 작성자id, 세션id 검증
        if (!AuthUtil.isAuthorized(session, board.getWriter().getId(), redirectAttributes)) {
            return "redirect:/boards/" + boardId;
        }

        board.updateFrom(form);
        boardService.writeBoard(board);
        return "redirect:/boards/" + boardId;
    }

    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable("boardId") Long boardId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Board board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!AuthUtil.isAuthorized(session, board.getWriter().getId(), redirectAttributes)) {
            return "redirect:/boards/" + boardId;
        }

        boardService.deleteBoard(boardId);
        return "redirect:/";
    }

}
