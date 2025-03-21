package codesquad.codestagram.article.dto;

public record ArticleRequest(
        String writer,
        String title,
        String content
) {
}
