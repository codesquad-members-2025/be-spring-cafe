package codesquad.codestagram.integration;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.UserRepository;
import codesquad.codestagram.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BoardIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired private BoardService boardService;

    private User dummyUser;


    @BeforeEach
    public void setUp() {
        dummyUser = new User("javajigi", "자바지기", "test", "javajigi@slipp.net");
        userRepository.save(dummyUser);

        boardRepository.deleteAll();
    }
    @Test
    @DisplayName("게시글 목록 페이지 테스트")
    public void testListBoards() throws Exception {
        // Given: 두 개의 게시글 생성
        Board board1 = new Board("제목1", "내용1");
        board1.setWriter(dummyUser);
        Board board2 = new Board("제목2", "내용2");
        board2.setWriter(dummyUser);
        boardRepository.save(board1);
        boardRepository.save(board2);

        // When & Then: GET "/" 요청시 index 뷰와 모델 속성 검증
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) //200 OK
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("boards"))
                .andExpect(model().attribute("boards", hasSize(2)));
    }
    @Test
    @DisplayName("게시글 작성 폼 페이지 테스트 (로그인 필요)")
    public void testNewBoardFormWithoutLogin() throws Exception {
        // When & Then: 로그인하지 않으면 "/users/login"으로 리다이렉션
        mockMvc.perform(get("/boards/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    public void testCreateBoard() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", dummyUser);

        mockMvc.perform(post("/boards/create")
                        .session(session)
                        .param("title", "새 게시글")
                        .param("content", "새 게시글 내용"))
                .andExpect(status().is3xxRedirection()) //응답 -> 3xx
                .andExpect(redirectedUrl("/"));

        // DB에서 생성된 게시글 확인
        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);
        Board createdBoard = boards.get(0);
        assertThat(createdBoard.getTitle()).isEqualTo("새 게시글");
        assertThat(createdBoard.getWriter().getLoginId()).isEqualTo(dummyUser.getLoginId());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void testUpdateBoardSuccess() throws Exception {
        // given
        Board board = new Board("원본 제목", "원본 내용");
        board.setWriter(dummyUser);
        board = boardService.writeBoard(board);
        Long boardId = board.getBoardId();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", dummyUser);

        // when
        mockMvc.perform(put("/boards/" + boardId + "/edit")
                        .session(session)
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + boardId));

        // then
        Board updatedBoard = boardService.getBoardById(boardId).orElseThrow(); //orElseThrow(): Optional에 값이 존재하지 않을 경우, 예외를 발생
        assertThat(updatedBoard.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedBoard.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void testDeleteBoardSuccess() throws Exception {
        // given
        Board board = new Board("제목", "내용");
        board.setWriter(dummyUser);
        board = boardService.writeBoard(board);
        Long boardId = board.getBoardId();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", dummyUser);

        // when
        mockMvc.perform(delete("/boards/" + boardId)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // then
        Optional<Board> deletedBoard = boardService.getBoardById(boardId);
        assertThat(deletedBoard).isEmpty();
    }

    //todo 실패 테스트도 작성하기


}