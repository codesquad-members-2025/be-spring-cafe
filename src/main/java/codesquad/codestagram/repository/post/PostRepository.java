package codesquad.codestagram.repository.post;

import codesquad.codestagram.entity.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
}
