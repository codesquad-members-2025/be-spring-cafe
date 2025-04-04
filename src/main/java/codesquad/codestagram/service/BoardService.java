package codesquad.codestagram.service;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.BoardAndReplyAuthorMismatchException;
import codesquad.codestagram.exception.UserNotAuthorException;
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

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }


    public Optional<Board> getBoardById(Long boardId){
        return boardRepository.findById(boardId);
    }

    @Transactional
    public void deleteBoard(Long boardId, User currentUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

         boardRepository.findByBoardIdAndWriter(boardId, currentUser)
                 .orElseThrow(UserNotAuthorException::new);
         //삭제되지 않은(active) 댓글들을 조회
        List<Reply> activeReplies = replyRepository.findByBoardAndDeletedFalse(board);
        if (!activeReplies.isEmpty()) {
            boolean allRepliesByBoardWriter = activeReplies.stream()
                    .allMatch(reply -> reply.getWriter().getId().equals(board.getWriter().getId()));
            if (!allRepliesByBoardWriter) {
                throw new BoardAndReplyAuthorMismatchException();
            }
        }
        board.setDeleted(true);
        board.getReplies().forEach(reply -> reply.setDeleted(true));
        boardRepository.save(board);

    }


}

