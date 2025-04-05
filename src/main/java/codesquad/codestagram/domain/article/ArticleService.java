package codesquad.codestagram.domain.article;

import codesquad.codestagram.domain.article.exception.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.reply.Reply;
import codesquad.codestagram.domain.reply.ReplyRepository;
import codesquad.codestagram.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    public ArticleService(ArticleRepository articleRepository, ReplyRepository replyRepository) {
        this.articleRepository = articleRepository;
        this.replyRepository = replyRepository;
    }

    @Transactional(readOnly = true)
    public Page<Article> findArticles(Pageable pageable) {
        return articleRepository.findByDeleted(false, pageable);
    }

    @Transactional
    public Article createArticle(String title, String content, User currentUser) {
        Article article = new Article(currentUser.getId(), title, content);

        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("게시물을 찾을 수 없습니다."));
    }

    // 로그인 여부, 게시물 존재, 작성자 일치 여부를 검증 후 게시물 반환
    @Transactional(readOnly = true)
    public Article getAuthorizedArticle(Long id, User currentUser) {
        Article article = findArticle(id);
        if (!article.isSameWriter(currentUser.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        return article;
    }

    @Transactional
    public Article updateArticle(Long id, String title, String content, User currentUser) {
        Article article = getAuthorizedArticle(id, currentUser);
        article.setTitle(title);
        article.setContent(content);

        return article;
    }

    @Transactional
    public void deleteArticle(Long id, User currentUser) {
        Article article = getAuthorizedArticle(id, currentUser);
        List<Reply> replies = replyRepository.findByArticleIdAndDeletedFalse(id);

        // 댓글이 존재하는 경우
        if (!replies.isEmpty()) {
            boolean allRepliesBySameAuthor = replies.stream()
                    .allMatch(reply -> reply.isSameWriter(currentUser.getId()));

            if (!allRepliesBySameAuthor) {
                throw new UnauthorizedException("작성자 외 댓글이 있어 삭제할 수 없습니다.");
            }

            // 댓글 삭제 처리
            for (Reply reply : replies) {
                reply.delete();
            }

            replyRepository.saveAll(replies);
        }

        // 게시글 삭제 처리
        article.delete();
    }

}
