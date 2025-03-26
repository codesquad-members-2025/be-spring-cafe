package codesquad.codestagram.controller;

import codesquad.codestagram.entity.Post;
import codesquad.codestagram.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "user/index";
    }

    public void init(){
        postService.initExamplePosts();
    }
}
