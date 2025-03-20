package codesquad.codestagram.service;

import codesquad.codestagram.Entity.Board;
import codesquad.codestagram.Entity.Comment;
import codesquad.codestagram.Entity.User;
import codesquad.codestagram.dto.CommentRequestDto;
import codesquad.codestagram.dto.CommentResponseDto;
import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.CommentRepository;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private CommentRepository commentRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          BoardRepository boardRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public void createComment(CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(String.valueOf(commentRequestDto.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found. userId: " + commentRequestDto.getUserId()));

        Board board = boardRepository.findById(commentRequestDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found. boardId: " + commentRequestDto.getBoardId()));

        Comment comment = new Comment(commentRequestDto.getContent(), user, board);
        commentRepository.save(comment);
    }
    public List<CommentResponseDto> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        List<CommentResponseDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentDtos.add(new CommentResponseDto(comment));
        }

        return commentDtos;
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
