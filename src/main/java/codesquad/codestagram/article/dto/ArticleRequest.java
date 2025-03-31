package codesquad.codestagram.article.dto;

public record ArticleRequest(
        String writerId,
        String title,
        String contents
) {
}
