package codesquad.codestagram.repository.post;

import codesquad.codestagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostRepository {
}
