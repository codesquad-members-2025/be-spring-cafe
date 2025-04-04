package codesquad.codestagram.integration;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.ReplyRepository;
import codesquad.codestagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReplyIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReplyRepository replyRepository;

    private User user;
    private Board board;
    private MockHttpSession session;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "테스트 사용자", "testpass", "testuser@example.com");
        userRepository.save(user);

        board = new Board("테스트 제목", "테스트 내용");
        board.setWriter(user);
        boardRepository.save(board);

        // 세션에 사용자 정보 설정
        session = new MockHttpSession();
        session.setAttribute("user", user);
    }

    @Test
    @DisplayName("댓글 추가 테스트")
    public void testAddReply() throws Exception {
        // 댓글 추가 (POST /boards/{boardId}/replies)
        mockMvc.perform(post("/boards/" + board.getBoardId() + "/replies")
                        .session(session)
                        .param("content", "댓글 내용 테스트"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + board.getBoardId()));

        // DB
        Board updatedBoard = boardRepository.findById(board.getBoardId())
                .orElseThrow();
        assertThat(updatedBoard.getReplies()).hasSize(1);
        Reply reply = updatedBoard.getReplies().get(0);
        assertThat(reply.getContent()).isEqualTo("댓글 내용 테스트");
        assertThat(reply.getWriter().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void testDeleteReply() throws Exception {
        // 먼저 댓글 추가
        Reply reply = new Reply("삭제할 댓글", user, board);
        board.addReply(reply); // 양방향 관계를 위해 board에 추가
        replyRepository.save(reply);

        // 게시글의 댓글 목록에 해당 댓글이 있는지 검증
        Board updatedBoard = boardRepository.findById(board.getBoardId())
                .orElseThrow();
        assertThat(updatedBoard.getReplies()).hasSize(1);

        // 댓글 삭제 (DELETE /boards/{boardId}/replies/{replyId})
        mockMvc.perform(delete("/boards/" + board.getBoardId() + "/replies/" + reply.getId())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + board.getBoardId()));

        // DB
        updatedBoard = boardRepository.findById(board.getBoardId())
                .orElseThrow();
        assertThat(updatedBoard.getReplies()).isEmpty();
        assertThat(replyRepository.findById(reply.getId())).isEmpty();
    }
}