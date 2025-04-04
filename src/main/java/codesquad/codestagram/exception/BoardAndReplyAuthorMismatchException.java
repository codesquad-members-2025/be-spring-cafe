package codesquad.codestagram.exception;

public class BoardAndReplyAuthorMismatchException extends RuntimeException {
    public BoardAndReplyAuthorMismatchException() {

        super("게시글 작성자와 댓글 작성자가 모두 동일한 경우에만 삭제할 수 있습니다.");
    }
}
