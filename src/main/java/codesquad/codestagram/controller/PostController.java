package codesquad.codestagram.controller;

import codesquad.codestagram.dto.PostRequestDto;
import codesquad.codestagram.entity.Post;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/page/1";
    }

//    public void init(){
//        postService.initExamplePosts();
//    }

    @GetMapping("/questions")
    public String questions() {
        return "qna/form";
    }

    @PostMapping("/questions")
    public String createPost(HttpSession session,
                             @ModelAttribute PostRequestDto dto) {
        User user = (User) session.getAttribute("loginUser");
        postService.savePost(dto, user);
        return "redirect:/";
    }

    @GetMapping("/boards/{id}")
    public String board(@PathVariable Long id,
                        Model model,
                        HttpSession session) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);

        User loginUser = (User) session.getAttribute("loginUser");
        boolean isOwner = loginUser != null && postService.isWriter(id, loginUser.getId());
        model.addAttribute("isOwner", isOwner);

        return "qna/show";
    }

    @DeleteMapping("/boards/{id}")
    public String deleteBoard(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        String redirect = checkPostAccess(id, loginUser);
        if (redirect != null) return redirect;

        postService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/boards/{id}/edit")
    public String editform(@PathVariable Long id,
                           Model model,
                           HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        String redirect = checkPostAccess(id, loginUser);
        if (redirect != null) return redirect;

        model.addAttribute("post", postService.getPost(id));
        return "qna/change";
    }

    @PutMapping("/boards/{id}")
    public String updateBoard(@PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam String content,
                              HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
        String redirect = checkPostAccess(id, loginUser);
        if (redirect != null) return redirect;

        postService.updatePost(id, title, content);
        return "redirect:/boards/" + id;
    }

    private String checkPostAccess(Long postId, User loginUser) {
        if (loginUser == null) return "redirect:/login";
        if (!postService.isWriter(postId, loginUser.getId())) {
            return "redirect:/boards/" + postId + "?error=unauthorized";
        }
        return null;
    }

    @GetMapping("/page/{id}")
    public String getListPage(Model model, @PathVariable Long id) {
        int pageSize = 15;
        Page<Post> posts = postService.getPage(id);

        int pageCount = 5;
        long totalPosts = posts.getTotalElements();
        int totalPages = posts.getTotalPages();
        int currentPage = id.intValue();

        int startPage = ((currentPage - 1) / pageCount) * pageCount + 1;
        int endPage = Math.min(startPage + pageCount - 1, totalPages);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "user/index";
    }
}
