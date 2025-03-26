package codesquad.codestagram.service;

import codesquad.codestagram.entity.Post;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.post.PostRepository;
import codesquad.codestagram.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    @PostConstruct
    public void initExamplePosts(){
        User user = userRepository.save(new User("배찌", "배찌", "w@w","배찌"));
        postRepository.save(new Post("안녕", "내", user));
    }
}
