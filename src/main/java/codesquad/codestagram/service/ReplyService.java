package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ReplyViewDto;
import codesquad.codestagram.exception.ReplyNotFoundException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.repository.ReplyRepository;
import codesquad.codestagram.utility.TextUtility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    public ReplyService(ReplyRepository replyRepository, ArticleService articleService) {
        this.replyRepository = replyRepository;
        this.articleService = articleService;
    }

    public Reply addReply(User user, Long index, String text) {
        Article article = articleService.findArticleById(index);
        Reply reply = new Reply(user,article,text);
        return replyRepository.save(reply);
    }

    public List<ReplyViewDto> findRepliesByArticle(Article article) {
        List<Reply> replyList = replyRepository.findByArticle(article);
        List<ReplyViewDto> replyDtoList = new ArrayList<>();
        for (Reply reply : replyList) {
            ReplyViewDto replyDto = new ReplyViewDto(
                    reply.getId(),
                    reply.getUser().getUserId(),
                    reply.getUser().getName(),
                    TextUtility.escapeAndConvertNewlines(reply.getText()),
                    reply.getCreatedAt());
            replyDtoList.add(replyDto);
        }
        return replyDtoList;
    }

    public void deleteReply(User user, Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException());
        if(!reply.isAuthor(user)){
            throw new UnauthorizedAccessException("이 댓글을 지울 권한이 없습니다.");
        };
        replyRepository.delete(reply);
    }
}
