package codesquad.codestagram.domain.reply;

import codesquad.codestagram.domain.article.ArticleRepository;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.exception.ReplyNotFoundException;
import codesquad.codestagram.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleRepository articleRepository;

    public ReplyService(ReplyRepository replyRepository, ArticleRepository articleRepository) {
        this.replyRepository = replyRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Reply addReply(Long articleId, User user, String content) {
        if (!articleRepository.existsById(articleId)) {
            throw new ReplyNotFoundException("게시물이 존재하지 않습니다.");
        }
        Reply reply = new Reply(user, articleId, content);

        return replyRepository.save(reply);
    }

    @Transactional(readOnly = true)
    public Page<Reply> findRepliesByArticle(Long articleId, Pageable pageable) {
        return replyRepository.findByArticleIdAndDeletedFalse(articleId, pageable);
    }

    @Transactional
    public void deleteReply(Long replyId, User user) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("댓글을 찾을 수 없습니다."));
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        reply.delete();
        replyRepository.save(reply);
    }

}
