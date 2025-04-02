package codesquad.codestagram.service;

import codesquad.codestagram.dto.PostRequestDto;
import codesquad.codestagram.entity.Post;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.post.PostRepository;
import codesquad.codestagram.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    @PostConstruct
//    public void initExamplePosts(){
//        User user = userRepository.save(new User("배찌", "배찌", "w@w","배찌"));
//        postRepository.save(new Post("안녕", "내", user));
//    }

    public void savePost(PostRequestDto dto, User user){
        postRepository.save(new Post(dto.getTitle(), dto.getContent(), user));
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
    }

    public boolean isWriter(Long postId, Long userId) {
        Post post = getPost(postId);
        return post.getUser().getId().equals(userId);
    }

    public void deletePost(Long id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long id, String title, String content) {
        Post post = getPost(id);
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }
}
