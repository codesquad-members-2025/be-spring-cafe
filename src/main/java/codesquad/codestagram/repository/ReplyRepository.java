package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    Reply save(Reply reply);
    List<Reply> findByArticle(Article article);
    Optional<Reply> findById(Long id);
    void delete(Reply reply);
}
