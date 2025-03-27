package codesquad.codestagram.dto;

import codesquad.codestagram.entity.Post;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String date;
    private Long writerId;
    private String writerName;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.date = post.getDate();
        this.writerId = post.writerId();
        this.writerName = post.writerName();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public Long getWriterId() {
        return writerId;
    }

    public String getWriterName() {
        return writerName;
    }
}
