package codesquad.codestagram.service;

import codesquad.codestagram.dto.PostRequestDto;
import codesquad.codestagram.entity.Post;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.post.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

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

    public Page<Post> getPage(Long id) {
        return postRepository.findPageBy(PageRequest.of((int) (id-1L), 5));
    }
}
