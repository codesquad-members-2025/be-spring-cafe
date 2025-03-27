package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ReplyRepository;
import java.util.List;
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
        return replyRepository.findAllByArticle(article);
    }
}
