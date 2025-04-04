package codesquad.codestagram.service;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import codesquad.codestagram.exception.BoardNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    //의존성 주입(DI)
    public BoardService(BoardRepository boardRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public Board writeBoard(Board board) {
        return boardRepository.save(board);
    }

    //todo 최적화: content는 가져올 필요 없음
    public List<Board> getAllBoards() {
        return boardRepository.findByDeletedFalse();
    }


    public Optional<Board> getBoardById(Long boardId){
        return boardRepository.findById(boardId);
    }

    @Transactional
    public boolean deleteBoard(Long boardId, User currentUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        if (boardRepository.findByBoardIdAndWriter(boardId, currentUser).isEmpty()) {
            return false; //작성자와 현재 로그인한 사용자 확인
        }

         //삭제되지 않은(active) 댓글들을 조회
        List<Reply> activeReplies = replyRepository.findByBoardAndDeletedFalse(board);
        if (!activeReplies.isEmpty()) {
            boolean allRepliesByBoardWriter = activeReplies.stream()
                    .allMatch(reply -> reply.getWriter().getId().equals(board.getWriter().getId()));
            if (!allRepliesByBoardWriter) {
                return false;
            }
        }
        board.setDeleted(true);
        board.getReplies().forEach(reply -> reply.setDeleted(true));
        boardRepository.save(board);
        return true;

    }


}

