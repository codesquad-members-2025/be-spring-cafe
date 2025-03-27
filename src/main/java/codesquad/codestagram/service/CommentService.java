package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Comment;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Comment leaveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return article.getComments();
    }

    //댓글 수정
    public Comment updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.setComment(content);
        return commentRepository.save(comment);
    }


    //댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
