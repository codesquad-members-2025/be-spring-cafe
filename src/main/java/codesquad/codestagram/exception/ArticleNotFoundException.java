package codesquad.codestagram.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super("존재하지 않는 게시물입니다.");
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }
}
