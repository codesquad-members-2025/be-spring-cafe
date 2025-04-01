package codesquad.codestagram.service;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardServiceIntegrationTest {

    @Autowired private BoardService boardService;
    @Autowired private BoardRepository boardRepository;

    @BeforeEach
    void clear() {
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void 게시글_작성() {
        //given
        Board board = new Board("게시글", "게시글 작성 테스트");

        //when
        Board savedBoard = boardService.writeBoard(board);

        //then
        Board testboard = boardService.getBoardById(savedBoard.getBoardId()).get(); //DB에서 다시 꺼내서 비교
        assertThat(testboard.getTitle()).isEqualTo("게시글");

    }

    @Test
    @DisplayName("게시글 여러개 생성 테스트 -> ")
    void 게시글_여러개_생성() {
        //given
        Board board1 = new Board("첫 글", "내용1");
        Board board2 = new Board("두 번째 글", "내용2");

        boardService.writeBoard(board1);
        boardService.writeBoard(board2);

        //when
        List<Board> testboard = boardService.getAllBoards();
        assertThat(testboard.size()).isEqualTo(2);
    }


}
