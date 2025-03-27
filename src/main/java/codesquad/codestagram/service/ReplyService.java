package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ReplyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public void addReply(String content, Article findArticle, User user) {
        Reply reply = new Reply(content, findArticle, user);
        replyRepository.save(reply);
    }

    public List<Reply> findReplies(Article article) {
        return replyRepository.findAllByArticleNotDeleted(article);
    }

    public Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    public boolean isReplyAuthor(User user, Reply reply) {
        return user.matchId(reply.getUser().getId());
    }

    public void deleteReply(Reply reply) {
        reply.changeDeleteStatus(true);
        replyRepository.save(reply);
    }
}
