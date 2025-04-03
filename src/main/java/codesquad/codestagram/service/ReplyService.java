package codesquad.codestagram.service;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyForm;
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
    public Reply addReply(Long boardId, ReplyForm form, User writer) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
        Reply reply = new Reply(form.getContent(), writer, board);
        return replyRepository.save(reply);
    }

    //댓글 조회 : 특정 게시글에 달린 전체 댓글
    public List<Reply> getReplies(Board board) {
        return replyRepository.findByBoard(board);
    }

    //댓글 삭제: 댓글 id와 로그인한 사용자 확인
    @Transactional
    public boolean deleteReply(Long replyId, User writer) {
        //조건을 만족하는 댓글이 없을 수도 있기 때문에 Optional
        Optional<Reply> reply = replyRepository.findByIdAndWriter(replyId, writer);
        if (reply.isPresent()) {
            replyRepository.delete(reply.get());
            return true;
        }
        return false;
    }
}
