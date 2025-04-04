package codesquad.codestagram.reply.service;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.common.exception.error.ForbiddenException;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.common.exception.error.ResourceNotFoundException;
import codesquad.codestagram.reply.domain.Reply;
import codesquad.codestagram.reply.repository.ReplyRepository;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static codesquad.codestagram.article.service.ArticleService.ARTICLE_NOT_FOUND;
import static codesquad.codestagram.user.service.UserService.USER_NOT_FOUND;

@Service
public class ReplyService {

    private static final String REPLY_NOT_FOUND = "존재하지 않는 댓글입니다.";

    private final ReplyRepository replyRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ReplyService(ReplyRepository replyRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createReply(Long articleId, Long loggedInUserId, String content) {
        validateReplyContent(content);
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ResourceNotFoundException(ARTICLE_NOT_FOUND)
        );

        User user = userRepository.findById(loggedInUserId).orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND)
        );

        Reply reply = new Reply(article, user, content);
        replyRepository.save(reply);
    }

    @Transactional
    public void updateReply(Long replyId, Long loggedInUserId, String content) {
        validateReplyContent(content);
        Reply reply = findById(replyId);
        isExistsArticle(reply.getArticle());
        if(!reply.isWrittenBy(loggedInUserId)){
            throw new ForbiddenException("자신의 댓글만 수정할 수 있습니다.");
        }
        reply.updateContent(content);
    }

    @Transactional
    public void deleteReply(Long replyId, Long loggedInUserId) {
        Reply reply = findById(replyId);
        isExistsArticle(reply.getArticle());
        if(!reply.isWrittenBy(loggedInUserId)){
            throw new ForbiddenException("자신의 댓글만 삭제할 수 있습니다.");
        }
        replyRepository.delete(reply);
    }

    private Reply findById(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(
                () -> new ResourceNotFoundException(REPLY_NOT_FOUND)
        );
    }

    private void validateReplyContent(String content) {
        if (content.trim().isEmpty() || content == null) {
            throw new InvalidRequestException("내용을 입력해주세요");
        }
    }

    private void isExistsArticle(Article article) {
        if (article == null) {
            throw new ResourceNotFoundException(ARTICLE_NOT_FOUND);
        }
    }
}
