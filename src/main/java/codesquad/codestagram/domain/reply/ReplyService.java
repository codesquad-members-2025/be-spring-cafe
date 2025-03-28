package codesquad.codestagram.domain.reply;

import codesquad.codestagram.domain.article.Article;
import codesquad.codestagram.domain.article.ArticleService;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.exception.ReplyNotFoundException;
import codesquad.codestagram.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    public ReplyService(ReplyRepository replyRepository, ArticleService articleService) {
        this.replyRepository = replyRepository;
        this.articleService = articleService;
    }

    public Reply addReply(Long articleId, User user, String content) {
        Article article = articleService.findArticle(articleId);
        Reply reply = new Reply(article.getId(), user.getId(), content);

        return replyRepository.save(reply);
    }

    public List<Reply> findRepliesByArticle(Long articleId) {
        Article article = articleService.findArticle(articleId);

        return replyRepository.findByArticleIdAndDeletedFalse(article.getId());
    }

    public void deleteReply(Long replyId, User user) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("댓글을 찾을 수 없습니다."));
        if (!reply.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        reply.delete();
        replyRepository.save(reply);
    }

}
