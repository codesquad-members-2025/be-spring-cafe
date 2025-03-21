package codesquad.codestagram.service;

import codesquad.codestagram.repository.MemoryBoardRepository;
import codesquad.codestagram.domain.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardServiceTest {
    private BoardService boardService;
    private MemoryBoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardRepository = new MemoryBoardRepository(); // 테스트용 메모리 저장소 초기화
        boardService = new BoardService(boardRepository); // 서비스에 저장소 주입
    }

    @AfterEach
    public void afterEach() {
        boardRepository.clearStore();
    }

    @Test
    void 게시글_작성_테스트() {
        // Given (테스트 데이터 생성)
        Board board = new Board();
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setWriter("테스터");

        // When (게시글 저장)
        Board savedBoard = boardService.writeBoard(board);

        // Then (게시글이 정상적으로 저장되었는지 검증)
        assertThat(savedBoard.getBoardId()).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo("테스트 제목");
        assertThat(savedBoard.getContent()).isEqualTo("테스트 내용");
        assertThat(savedBoard.getWriter()).isEqualTo("테스터");
    }

    @Test
    void 게시글_목록_조회_테스트() {
        // Given (게시글 2개 저장)
        Board board1 = new Board();
        board1.setTitle("첫 번째 게시글");
        board1.setContent("첫 번째 내용");
        board1.setWriter("작성자1");
        boardService.writeBoard(board1);

        Board board2 = new Board();
        board2.setTitle("두 번째 게시글");
        board2.setContent("두 번째 내용");
        board2.setWriter("작성자2");
        boardService.writeBoard(board2);

        // When (목록 조회)
        List<Board> boards = boardService.getAllBoards();

        // Then (저장된 게시글이 2개인지 확인)
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards.get(0).getTitle()).isEqualTo("첫 번째 게시글");
        assertThat(boards.get(1).getTitle()).isEqualTo("두 번째 게시글");
    }


    @Test
    void 특정_게시글_조회_테스트() {
        // Given (게시글 저장)
        Board board = new Board();
        board.setTitle("조회할 게시글");
        board.setContent("조회할 내용");
        board.setWriter("작성자");
        Board savedBoard = boardService.writeBoard(board);

        // When (ID로 게시글 조회)
        Board foundBoard = boardService.getBoardById(savedBoard.getBoardId()).orElse(null);

        // Then (조회 결과 확인)
        assertThat(foundBoard).isNotNull();
        assertThat(foundBoard.getTitle()).isEqualTo("조회할 게시글");
        assertThat(foundBoard.getContent()).isEqualTo("조회할 내용");
        assertThat(foundBoard.getWriter()).isEqualTo("작성자");
    }



}
