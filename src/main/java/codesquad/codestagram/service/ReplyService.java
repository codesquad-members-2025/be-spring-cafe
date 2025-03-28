package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ReplyRepository;
import jakarta.servlet.http.HttpSession;
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
        return replyRepository.findAllByArticleNotDeleted(article);
    }

    public Reply findReplyByIdAndNotDeleted(Long replyId) {
        return replyRepository.findByIdAndNotDeleted(replyId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    public boolean isNotReplyAuthor(User user, Reply reply) {
        return !user.matchId(reply.getUser().getId());
    }

    public void deleteReply(Reply reply) {
        reply.changeDeleteStatus(true);
        replyRepository.save(reply);
    }

    public boolean checkCanDelete(Article article, HttpSession session) {
        List<Reply> replies = findReplies(article);
        //댓글이 없는 경우 삭제 가능
        if (replies.isEmpty()) return true;

        User user = (User) session.getAttribute(SESSIONED_USER);
        for (Reply reply : replies) {
            //게시글 작성자와 댓글 작성자가 다르면 삭제 불가
            if(isNotReplyAuthor(user, reply)) return false;
        }

        return true;
    }
}
