package codesquad.codestagram.repository.post;

import codesquad.codestagram.entity.Post;

import java.util.*;

public class PostMapRepository implements PostRepository {
    private static final Map<Long, Post> posts = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}
