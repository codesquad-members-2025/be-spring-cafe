package codesquad.codestagram.repository.post;

import codesquad.codestagram.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    void delete(Post post);
    Page<Post> findPageBy(Pageable page);
}
