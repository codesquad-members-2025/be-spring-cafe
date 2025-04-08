package codesquad.codestagram.service;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyForm;
import codesquad.codestagram.exception.ReplyNotFoundException;
import codesquad.codestagram.exception.BoardNotFoundException;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public ReplyService(ReplyRepository replyRepository,  BoardRepository boardRepository) {
        this.replyRepository = replyRepository;
        this.boardRepository = boardRepository;
    }

    //댓글작성(추가)
    @Transactional
    public void addReply(ReplyForm form, User writer, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
        Reply reply = new Reply(form.getContent(), writer, board);
        board.addReply(reply); //보드의 replyList에도 댓글 추가
        replyRepository.save(reply);
    }

    //댓글 조회 : 특정 게시글에 달린 전체 댓글
    public List<Reply> getReplies(Board board) {
        return replyRepository.findByBoard(board);
    }

    //댓글 조회 : 특정 게시글에 달린 "deleted == false"인 전체 댓글
    public List<Reply> getActiveReplies(Board board) {
        return replyRepository.findByBoardAndDeletedFalse(board);
    }

    public Optional<Reply> getReplyById(Long replyId) {
        return replyRepository.findById(replyId);
    }

    //댓글 수정
    @Transactional
    public boolean updateReply(Long replyId, ReplyForm form, User writer) {
        Optional<Reply> optionalReply = replyRepository.findByIdAndWriter(replyId, writer);
        if (optionalReply.isPresent()) {
            Reply reply = optionalReply.get();
            reply.setContent(form.getContent());
            replyRepository.save(reply);
            return true;
        }
        return false;
    }

    //댓글 삭제: 댓글 id와 로그인한 사용자 확인
    @Transactional
    public void deleteReply(Long replyId, User currentUser) {
        Reply reply = replyRepository.findByIdAndWriterAndDeletedFalse(replyId, currentUser)
                .orElseThrow(ReplyNotFoundException::new);
        reply.setDeleted(true);
        replyRepository.save(reply);
    }
}
