package codesquad.codestagram.service;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.UserRepository;
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
    @Autowired private UserRepository userRepository;

    private User dummyUser;

    @BeforeEach
    void setUp() {
        // 더미 User 객체 생성
        dummyUser = new User("dummy", "더미사용자", "test", "dummy@example.com");
        userRepository.save(dummyUser);

        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void 게시글_작성() {
        //given
        // 실제 애플리케이션에서는 작성자 정보는 세션에서 가져오지만, 테스트에서는 더미 User를 설정
        Board board = new Board("게시글", "게시글 작성 테스트");
        board.setWriter(dummyUser); // writer를 더미 User 객체로 설정

        //when
        Board savedBoard = boardService.writeBoard(board);

        //then
        Board testboard = boardService.getBoardById(savedBoard.getBoardId()).get(); //DB에서 다시 꺼내서 비교
        assertThat(testboard.getTitle()).isEqualTo("게시글");
        assertThat(testboard.getWriter().getLoginId()).isEqualTo(dummyUser.getLoginId());

    }

    @Test
    @DisplayName("게시글 여러개 생성 테스트 -> ")
    void 게시글_여러개_생성() {
        //given
        Board board1 = new Board("첫 글", "내용1");
        board1.setWriter(dummyUser);
        Board board2 = new Board("두 번째 글", "내용2");
        board2.setWriter(dummyUser);

        boardService.writeBoard(board1);
        boardService.writeBoard(board2);

        //when
        List<Board> boards = boardService.getAllBoards();
        //then
        assertThat(boards.size()).isEqualTo(2);
    }


}
