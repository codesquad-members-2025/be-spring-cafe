package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;

import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);
    List<Reply> findByArticle(Article article);
    void delete(Reply reply);
}
