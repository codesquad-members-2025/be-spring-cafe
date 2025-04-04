package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.ReplyNotFoundException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    public ReplyService(ReplyRepository replyRepository, ArticleService articleService) {
        this.replyRepository = replyRepository;
        this.articleService = articleService;
    }

    @Transactional
    public Reply addReplyToArticle(User user, Long index, String text) {
        Article article = articleService.findArticleById(index);
        Reply reply = article.addReply(user, text);
        return replyRepository.save(reply);
    }

    @Transactional
    public void deleteReply(Reply reply) {
        reply.softDelete();
    }

    public Reply findReplyIfOwner(User user, Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException());
        if(!reply.isAuthor(user)){
            throw new UnauthorizedAccessException("댓글의 작성자가 아닙니다.");
        }
        return reply;
    }
}
