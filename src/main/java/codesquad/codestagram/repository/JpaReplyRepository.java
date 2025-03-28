package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaReplyRepository extends ReplyRepository, JpaRepository<Reply, Long> {

    @Override
    Reply save(Reply reply);

    @Override
    List<Reply> findByArticle(Article article);

    @Override
    void delete(Reply reply);

    @Override
    Optional<Reply> findById(Long id);
}
