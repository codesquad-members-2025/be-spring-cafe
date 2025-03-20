package codesquad.codestagram.controller;

import codesquad.codestagram.dto.CommentRequestDto;
import codesquad.codestagram.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String createComment(@ModelAttribute CommentRequestDto dto){
        commentService.createComment(dto);
        return "redirect:/boards/"+dto.getBoardId();
    }
}
