package codesquad.codestagram.config;

import codesquad.codestagram.repository.post.PostJpaRepository;
import codesquad.codestagram.repository.post.PostRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostConfig {
    private final PostJpaRepository postJpaRepository;

    public PostConfig(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }
    @Bean
    public PostRepository postRepository(){
        return postJpaRepository;
//        return new PostMapRepository();
    }
}
