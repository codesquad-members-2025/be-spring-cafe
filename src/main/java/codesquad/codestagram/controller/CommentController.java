package codesquad.codestagram.controller;

import codesquad.codestagram.dto.CommentRequestDto;
import codesquad.codestagram.dto.CommentResponseDto;
import codesquad.codestagram.entity.Comment;
import codesquad.codestagram.entity.Post;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.comment.CommentRepository;
import codesquad.codestagram.repository.post.PostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/boards/{id}/comments")
    public Comment save(@PathVariable Long id,
                       @RequestBody CommentRequestDto request,
                       HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Post post = postRepository.findById(id).orElse(null);
        Comment comment = new Comment(request.getContent(), user, post);

        return commentRepository.save(comment);
    }

    @GetMapping("/boards/{id}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        List<CommentResponseDto> result = new ArrayList<>();

        for (Comment comment : comments) {
            String content = comment.getComment();
            String writer = comment.getUser().getName();
            String date =  comment.getDate();
            Long writerId = comment.getUser().getId();

            CommentResponseDto commentResponseDto = new CommentResponseDto(content, writer, date, writerId);
            result.add(commentResponseDto);
        }

        return result;
    }
}
